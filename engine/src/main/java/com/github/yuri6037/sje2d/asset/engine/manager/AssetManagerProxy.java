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
import com.github.yuri6037.sje2d.asset.engine.system.IAsset;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.atomic.AtomicInteger;

public class AssetManagerProxy {
    private static final Logger LOGGER = LoggerFactory.getLogger(AssetManagerProxy.class);
    private final AssetMap map;
    private final ArrayBlockingQueue<Command> channel;
    private final AtomicInteger opCount;

    @SuppressWarnings("unchecked")
    private static <T extends IAsset> AssetStore<T> tryCast(final Class<T> c, final AssetStore<?> store) {
        try {
            c.cast(store.getAsset());
            return (AssetStore<T>) store;
        } catch (ClassCastException e) {
            return null;
        }
    }

    AssetManagerProxy(final AssetMap map, final ArrayBlockingQueue<Command> channel, final AtomicInteger opCount) {
        this.map = map;
        this.channel = channel;
        this.opCount = opCount;
    }

    /**
     * Creates a reference to an asset by its virtual path.
     *
     * @param c     the asset class, this is required because the java compiler has a defect.
     * @param vpath the virtual path of the asset.
     * @param <T>   the type of asset to return.
     * @return a reference to the asset as T or null if the virtual path could not be found or if the asset type does
     * not match.
     */
    public <T extends IAsset> AssetStore<T>.Ref get(final Class<T> c, final String vpath) {
        AssetStore<?> store = map.get(vpath);
        if (store == null) {
            return null;
        }
        AssetStore<T> store1 = AssetManagerProxy.tryCast(c, store);
        if (store1 == null) {
            return null;
        }
        return store1.new Ref();
    }

    /**
     * Gets all assets of a given type.
     *
     * @param c   the asset class, this is required because the java compiler has a defect.
     * @param <T> the type of asset to search for.
     * @return a list of all known assets of that type.
     */
    public <T extends IAsset> List<String> getAllAssetsOfType(final Class<T> c) {
        return map.filter((store) -> AssetManagerProxy.tryCast(c, store) != null);
    }

    /**
     * Gets all assets of a given type.
     *
     * @param c         the asset class, this is required because the java compiler has a defect.
     * @param namespace the namespace to restrict the search to.
     * @param <T>       the type of asset to search for.
     * @return a list of all known assets of that type in the specified namespace.
     */
    public <T extends IAsset> List<String> getAllAssetsOfType(final Class<T> c, final String namespace) {
        final String fuckingjava = namespace + "/"; //Add a '/' at the end of the namespace to mark the end of it.
        return map.filter((store) -> store.getVirtualPath().startsWith(fuckingjava) && AssetManagerProxy
                .tryCast(c, store) != null);
    }

    /**
     * Gets all assets in a given namespace.
     *
     * @param namespace the asset namespace.
     * @return a list of all assets found in the namespace.
     */
    public List<String> getAllAssetsInNamespace(final String namespace) {
        final String fuckingjava = namespace + "/"; //Add a '/' at the end of the namespace to mark the end of it.
        return map.filter((store) -> store.getVirtualPath().startsWith(fuckingjava));
    }

    /**
     * Checks whether a given asset is in use.
     * NOTE: This method does not check other assets which depends on this asset.
     *
     * @param vpath the virtual path of the asset.
     * @return true if the asset is in use, false otherwise.
     */
    public boolean isInUse(final String vpath) {
        //Check if this asset is in use.
        AssetStore<?> store = map.get(vpath);
        if (store == null) { //If the asset does not exist, then it can't be in use.
            return false;
        }
        return store.getUses() > 0;
    }

    private int queueOperation(final Command command) {
        int ops = opCount.get();
        try {
            channel.put(command);
            opCount.set(ops + 1);
            return ops + 1;
        } catch (InterruptedException e) {
            LOGGER.error("Failed to queue operation", e);
            return ops;
        }
    }

    /**
     * @return the number of operations that are being executed by the underlying AssetManager.
     */
    public int getOperationCount() {
        return opCount.get();
    }

    /**
     * @return the total number of usable and used assets.
     */
    public int getAssetsCount() {
        return map.len();
    }

    /**
     * Waits for all remaining operations to finish.
     */
    public void waitRemainingOperations() throws InterruptedException {
        while (getOperationCount() > 0) {
            //noinspection BusyWait
            Thread.sleep(100);
        }
    }

    /**
     * Adds a URL to the queue of assets that needs to be loaded.
     *
     * @param url the URL of the asset to load.
     * @return the number of queued operations
     */
    public int queue(final AssetURL url) {
        return queueOperation(new Command(Command.Type.Queue, url));
    }

    /**
     * Attempts to unload a namespace.
     *
     * @param namespace the namespace to unload.
     * @return the number of queued operations
     */
    public int unloadNamespace(final String namespace) {
        return queueOperation(new Command(Command.Type.UnloadNamespace, namespace));
    }

    /**
     * Attempts to unload a given asset.
     * NOTE: for safety reasons, this checks if the asset is currently in use or that it is locked.
     *
     * @param vpath the virtual path of the asset.
     * @return the number of queued operations
     */
    public int unload(final String vpath) {
        return queueOperation(new Command(Command.Type.Unload, vpath));
    }
}
