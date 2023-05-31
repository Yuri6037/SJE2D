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

package com.github.yuri6037.sje2d.asset.engine.map;

import com.github.yuri6037.sje2d.asset.engine.system.IAsset;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * This class is a global thread safe shared storage structure for all assets.
 */
public final class AssetMap {
    private final HashMap<String, AssetStore<? extends IAsset>> map = new HashMap<>();
    private final HashMap<String, Integer> lockMap = new HashMap<>();

    /**
     * Adds a new asset to this map.
     * @param store the asset store to add.
     */
    public void push(final AssetStore<?> store) {
        synchronized (map) {
            map.put(store.getVirtualPath(), store);
        }
    }

    /**
     * @return the total number of assets stored in this map.
     */
    public int len() {
        synchronized (map) {
            return map.size();
        }
    }

    /**
     * Removes an asset from this map.
     * @param vpath the virtual path of the asset to remove.
     */
    public void remove(final String vpath) {
        synchronized (map) {
            map.remove(vpath);
        }
    }

    /**
     * Gets an asset from this map.
     * @param vpath the virtual path of the asset to get.
     * @return null if the corresponding asset store could not be found, otherwise the associated asset store.
     */
    public AssetStore<?> get(final String vpath) {
        synchronized (map) {
            return map.get(vpath);
        }
    }

    /**
     * Gets an asset and locks it. A locked asset is an asset which has a special flag.
     * It is used while loading asset dependencies to prevent the asset manager from unloading or removing
     * the dependency of an asset.
     * If vpath cannot be found in this map, no lock is applied.
     * @param vpath the virtual path of the asset to get.
     * @return the matching asset store or null if not found.
     */
    public AssetStore<?> lock(final String vpath) {
        synchronized (map) {
            AssetStore<?> store = map.get(vpath);
            if (store != null) {
                synchronized (lockMap) {
                    if (!lockMap.containsKey(vpath)) {
                        lockMap.put(vpath, 1);
                    } else {
                        lockMap.put(vpath, lockMap.get(vpath) + 1);
                    }
                }
                return store;
            }
            return null;
        }
    }

    /**
     * Filters this map with a condition.
     * @param filter the condition to apply to each element to know if this element should be part of the filtered list.
     * @return a list of all items matching the given condition.
     */
    public List<String> filter(final IFilter filter) {
        ArrayList<String> list = new ArrayList<>();
        synchronized (map) {
            for (AssetStore<?> value : map.values()) {
                if (filter.accept(value)) {
                    list.add(value.getVirtualPath());
                }
            }
        }
        return list;
    }

    /**
     * Unlocks a given asset virtual path. This has no effect if the asset is not already locked.
     * @param vpath the virtual path of the asset to unlock.
     */
    public void unlock(final String vpath) {
        synchronized (lockMap) {
            Integer value = lockMap.get(vpath);
            if (value != null && value > 1) {
                lockMap.put(vpath, value - 1);
            } else {
                lockMap.remove(vpath);
            }
        }
    }

    /**
     * Checks whether an asset is locked.
     * @param vpath the virtual path of the asset.
     * @return true if the asset is locked, false otherwise.
     */
    public boolean isLocked(final String vpath) {
        synchronized (lockMap) {
            return lockMap.containsKey(vpath);
        }
    }
}
