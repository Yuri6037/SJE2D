/* 
 *  Copyright (c) 2023, SJE2D
 *  
 *  All rights reserved.
 *  
 *  Redistribution and use in source and binary forms, with or without modification,
 *  are permitted provided that the following conditions are met:
 *  
 *      * Redistributions of source code must retain the above copyright notice,
 *        this list of conditions and the following disclaimer.
 *      * Redistributions in binary form must reproduce the above copyright notice,
 *        this list of conditions and the following disclaimer in the documentation
 *        and/or other materials provided with the distribution.
 *      * Neither the name of BlockProject 3D nor the names of its contributors
 *        may be used to endorse or promote products derived from this software
 *        without specific prior written permission.
 *  
 *  THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
 *  "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT
 *  LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR
 *  A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR
 *  CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL,
 *  EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO,
 *  PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR
 *  PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF
 *  LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
 *  NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 *  SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

package com.github.yuri6037.sje2d.asset.engine.manager;

import com.github.yuri6037.sje2d.asset.engine.map.AssetMap;
import com.github.yuri6037.sje2d.asset.engine.AssetURL;
import com.github.yuri6037.sje2d.asset.engine.system.ITypeRegistry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

final class AssetSchedulerThread implements Runnable {
    private static final Logger LOGGER = LoggerFactory.getLogger(AssetSchedulerThread.class);
    private final ExecutorService service = Executors.newWorkStealingPool(4);
    private final ArrayList<Future<AssetLoadTask>> queue = new ArrayList<>();
    private final ArrayBlockingQueue<AssetURL> inChannel;
    private final ArrayBlockingQueue<AssetLoadTask.Result> outChannel;
    private final ITypeRegistry registry;
    private final AssetMap map;

    private AssetSchedulerThread(final ITypeRegistry registry, final AssetMap map,
                                 final ArrayBlockingQueue<AssetURL> inChannel,
                                 final ArrayBlockingQueue<AssetLoadTask.Result> outChannel) {
        this.registry = registry;
        this.map = map;
        this.inChannel = inChannel;
        this.outChannel = outChannel;
    }

    public static Thread create(final ITypeRegistry registry, final AssetMap map,
                                final ArrayBlockingQueue<AssetURL> inChannel,
                                final ArrayBlockingQueue<AssetLoadTask.Result> outChannel) {
        Thread thread = new Thread(new AssetSchedulerThread(registry, map, inChannel, outChannel));
        thread.setName("Asset Scheduler");
        thread.start();
        return thread;
    }

    private void emptyInChannel() {
        try {
            while (!inChannel.isEmpty()) {
                AssetURL url = inChannel.take();
                queue.add(service.submit(new AssetLoadTask(registry, map, url)));
            }
        } catch (InterruptedException e) {
            LOGGER.error("An error has occurred while emptying the input channel", e);
        }
    }

    @Override
    public void run() {
        LOGGER.info("Starting asset scheduler thread");
        LOGGER.debug("Solver V{}", Constants.VERSION);
        emptyInChannel();
        while (!queue.isEmpty()) {
            for (int i = queue.size() - 1; i != -1; --i) {
                Future<AssetLoadTask> item = queue.get(i);
                if (item.isDone()) {
                    queue.remove(i);
                    try {
                        AssetLoadTask res = item.get();
                        if (res.isNone()) {
                            LOGGER.info("Dropped asset '{}': loader won't produce any asset", res);
                            continue;
                        }
                        AssetLoadTask.Result res1 = res.tryFinish();
                        if (res1 != null) {
                            LOGGER.info("Loaded asset '{}'", res);
                            outChannel.put(res1);
                        } else {
                            if (res.isAlive()) {
                                emptyInChannel();
                                queue.add(service.submit(res));
                            } else {
                                LOGGER.error("Failed to resolve dependencies for '{}'", res);
                            }
                        }
                    } catch (InterruptedException | ExecutionException e) {
                        LOGGER.error("The scheduler has been interrupted, terminating...", e);
                        return;
                    }
                }
            }
            emptyInChannel();
        }
        LOGGER.info("Terminating asset scheduler thread");
    }
}
