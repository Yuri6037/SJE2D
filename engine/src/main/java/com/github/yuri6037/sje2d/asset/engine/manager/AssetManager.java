/*
 * Copyright (c) 2023, SJE2D
 *
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without modification,
 * are permitted provided that the following conditions are met:
 *
 *     * Redistributions of source code must retain the above copyright notice,
 *       this list of conditions and the following disclaimer.
 *     * Redistributions in binary form must reproduce the above copyright notice,
 *       this list of conditions and the following disclaimer in the documentation
 *       and/or other materials provided with the distribution.
 *     * Neither the name of BlockProject 3D nor the names of its contributors
 *       may be used to endorse or promote products derived from this software
 *       without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
 * "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT
 * LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR
 * A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR
 * CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL,
 * EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO,
 * PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR
 * PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF
 * LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
 * NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

package com.github.yuri6037.sje2d.asset.engine.manager;

import com.github.yuri6037.sje2d.asset.engine.map.AssetMap;
import com.github.yuri6037.sje2d.asset.engine.map.AssetStore;
import com.github.yuri6037.sje2d.asset.engine.AssetURL;
import com.github.yuri6037.sje2d.asset.engine.system.ITypeRegistry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.atomic.AtomicInteger;

public class AssetManager {
    private static final Logger LOGGER = LoggerFactory.getLogger(AssetManager.class);

    private final ITypeRegistry registry;
    private final AssetMap map = new AssetMap();
    private final HashMap<String, ArrayList<String>> dependentsMap = new HashMap<>();
    private final ArrayBlockingQueue<Command> channel = new ArrayBlockingQueue<>(128);
    private final ArrayBlockingQueue<AssetURL> schedulerInChannel = new ArrayBlockingQueue<>(64);
    private final ArrayBlockingQueue<AssetLoadTask.Result> schedulerOutChannel = new ArrayBlockingQueue<>(64);
    private Thread schedulerThread = null;
    private final AtomicInteger opCount = new AtomicInteger(0);

    /**
     * Creates a new AssetManager with the given type registry.
     * @param registry the type registry.
     */
    public AssetManager(final ITypeRegistry registry) {
        this.registry = registry;
    }

    private boolean isInUse(final String vpath) {
        //Check if this asset is in use.
        AssetStore<?> store = map.get(vpath);
        if (store == null) { //If the asset does not exist, then it can't be in use.
            return false;
        }
        if (store.getUses() > 0) {
            return true;
        }
        //Check if any asset which has this asset as dependency is still in use.
        List<String> deps = dependentsMap.get(vpath);
        if (deps == null) { //If there are no dependents, return early.
            return false;
        }
        for (String v : deps) {
            if (isInUse(v)) {
                return true;
            }
        }
        return false;
    }

    private boolean isLocked(final String vpath) {
        if (map.isLocked(vpath)) {
            return true;
        }
        List<String> deps = dependentsMap.get(vpath);
        if (deps != null) {
            for (String v: deps) {
                if (isLocked(v)) {
                    return true;
                }
            }
        }
        return false;
    }

    private void unloadUnchecked(final String vpath) {
        AssetStore<?> store = map.get(vpath);
        if (store == null) {
            return;
        }
        //Unload all assets that has this asset as dependency.
        List<String> deps = dependentsMap.get(vpath);
        if (deps != null) {
            for (String v: deps) {
                unloadUnchecked(v);
            }
        }
        //Unload this asset.
        try {
            store.getAsset().unload();
        } finally {
            map.remove(vpath);
            dependentsMap.remove(vpath);
            // TODO: possible memory leak: if the leaf asset is unloaded first, then the dependentsMap may contain
            //  references to the leaf asset if the leaf assets has dependencies
            LOGGER.info("Unloaded asset '{}'", vpath);
        }
    }

    private boolean unload(final String vpath) {
        if (isLocked(vpath)) {
            LOGGER.error("Failed to unload '{}': asset is locked", vpath);
            return false;
        }
        if (isInUse(vpath)) {
            LOGGER.error("Failed to unload '{}': asset still in use", vpath);
            return false;
        }
        unloadUnchecked(vpath);
        return true;
    }

    private List<String> getAllAssetsInNamespace(final String namespace) {
        final String fuckingjava = namespace + "/"; //Add a '/' at the end of the namespace to mark the end of it.
        return map.filter((store) -> store.getVirtualPath().startsWith(fuckingjava));
    }

    private void unloadNamespace(final String namespace) {
        List<String> assets = getAllAssetsInNamespace(namespace);
        for (String vpath: assets) {
            if (isLocked(vpath)) {
                LOGGER.error("Failed to unload namespace '{}': namespace contains locked assets", namespace);
                return;
            }
            if (isInUse(vpath)) {
                LOGGER.error("Failed to unload namespace '{}': namespace has some assets which are still in use",
                        namespace);
                return;
            }
        }
        for (String vpath: assets) {
            if (!unload(vpath)) {
                LOGGER.warn("Some assets failed to unload in namespace '{}'", namespace);
            }
        }
    }

    private void queue(final AssetURL url) {
        if (schedulerThread == null || !schedulerThread.isAlive()) {
            schedulerThread = AssetSchedulerThread.create(registry, map, schedulerInChannel, schedulerOutChannel);
        }
        try {
            schedulerInChannel.put(url);
        } catch (InterruptedException e) {
            LOGGER.error("Failed to submit asset to scheduler", e);
        }
    }

    private void mountAsset(final AssetLoadTask.Result res) throws Exception {
        if (res == null) {
            return;
        }
        AssetStore<?> store = res.loader().create();
        for (String dep : res.deps()) {
            if (!dependentsMap.containsKey(dep)) {
                dependentsMap.put(dep, new ArrayList<>());
            }
            dependentsMap.get(dep).add(store.getVirtualPath());
            map.unlock(dep);
        }
        LOGGER.info("Mounted asset '{}'", store.getVirtualPath());
        map.push(store);
    }

    /**
     * Waits for all operations to finish.
     */
    public void waitAll() throws InterruptedException {
        while ((schedulerThread != null && schedulerThread.getState() != Thread.State.TERMINATED)
                || !schedulerOutChannel.isEmpty() || !channel.isEmpty()) {
            update();
            //noinspection BusyWait
            Thread.sleep(100);
        }
    }

    /**
     * Updates this AssetManager.
     */
    //Unfortunately java refuses to allow this only on the corresponding line so disable it for the entire function
    public void update() {
        if (!schedulerOutChannel.isEmpty()) {
            try {
                AssetLoadTask.Result res = schedulerOutChannel.take();
                mountAsset(res);
            } catch (Exception e) {
                LOGGER.error("Failed to mount asset", e);
            }
        }
        if (!channel.isEmpty()) {
            Command cmd = channel.poll();
            switch (cmd.type()) {
                case UnloadNamespace -> unloadNamespace((String) cmd.arg());
                case Unload -> unload((String) cmd.arg());
                case Queue -> queue((AssetURL) cmd.arg());
            }
            opCount.addAndGet(-1);
        }
    }

    /**
     * Creates a new proxy.
     * @return a new AssetManagerProxy.
     */
    public AssetManagerProxy newProxy() {
        return new AssetManagerProxy(map, channel, opCount);
    }
}
