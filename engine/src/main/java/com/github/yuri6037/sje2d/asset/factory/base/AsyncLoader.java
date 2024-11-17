/*
 * Copyright (c) 2024, SJE2D
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

package com.github.yuri6037.sje2d.asset.factory.base;

import com.github.yuri6037.sje2d.asset.engine.AssetURL;
import com.github.yuri6037.sje2d.asset.engine.map.AssetDepMap;
import com.github.yuri6037.sje2d.asset.engine.system.IAsset;

public abstract class AsyncLoader<T extends IAsset> extends BaseLoader<T> {
    private final Thread thread;
    private AssetDepMap deps1;
    private String[] neededDeps;
    private Exception threadException;

    /**
     * Creates a new AsyncLoader.
     *
     * @param url the asset url that is going to be loaded.
     */
    public AsyncLoader(final AssetURL url) {
        super(url);
        thread = new Thread(() -> {
            try {
                this.loadAsync();
            } catch (Exception e) {
                threadException = e;
            }
        });
        neededDeps = null;
    }

    /**
     * Awaits an asset dependency.
     * @param assetClass the asset type class to wait for.
     * @param vpath the virtual path of the required asset.
     * @return the found asset or null if not found.
     * @param <V> the asset generic type.
     */
    protected <V extends IAsset> V awaitAsset(final Class<V> assetClass, final String vpath) {
        V asset;
        synchronized (this) {
            asset = deps1.contains(vpath) ? deps1.get(assetClass, vpath) : null;
            if (asset == null) {
                neededDeps = new String[]{vpath};
            }
        }
        if (asset != null) {
            return asset;
        }
        while (true) {
            synchronized (this) {
                if (neededDeps == null) {
                    asset = deps1.contains(vpath) ? deps1.get(assetClass, vpath) : null;
                    break;
                }
            }
            Thread.yield();
        }
        return asset;
    }

    /**
     * Loads this asset asynchronously by allowing blocking-like behavior when requiring dependencies.
     * @throws Exception when this asset failed to load.
     */
    protected abstract void loadAsync() throws Exception;

    protected abstract T createAsset() throws Exception;

    @Override
    public final Result load(final AssetDepMap deps) throws Exception {
        if (deps1 == null) {
            thread.start();
        }
        synchronized (this) {
            deps1 = deps;
            neededDeps = null;
        }
        while (thread.isAlive()) {
            synchronized (this) {
                if (neededDeps != null) {
                    return Result.needsDependencies(neededDeps);
                }
            }
            Thread.yield();
        }
        if (threadException != null) {
            throw threadException;
        }
        return Result.ready();
    }
}
