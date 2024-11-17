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
