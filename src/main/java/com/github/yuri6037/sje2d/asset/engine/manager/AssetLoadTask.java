// Copyright (c) 2023, SJE2D
//
// All rights reserved.
//
// Redistribution and use in source and binary forms, with or without modification,
// are permitted provided that the following conditions are met:
//
//     * Redistributions of source code must retain the above copyright notice,
//       this list of conditions and the following disclaimer.
//     * Redistributions in binary form must reproduce the above copyright notice,
//       this list of conditions and the following disclaimer in the documentation
//       and/or other materials provided with the distribution.
//     * Neither the name of BlockProject 3D nor the names of its contributors
//       may be used to endorse or promote products derived from this software
//       without specific prior written permission.
//
// THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
// "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT
// LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR
// A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR
// CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL,
// EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO,
// PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR
// PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF
// LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
// NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
// SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.

package com.github.yuri6037.sje2d.asset.engine.manager;

import com.github.yuri6037.sje2d.asset.engine.AssetURL;
import com.github.yuri6037.sje2d.asset.engine.map.AssetDepMap;
import com.github.yuri6037.sje2d.asset.engine.map.AssetMap;
import com.github.yuri6037.sje2d.asset.engine.map.AssetStore;
import com.github.yuri6037.sje2d.asset.engine.system.IAssetFactory;
import com.github.yuri6037.sje2d.asset.engine.system.IAssetLoader;
import com.github.yuri6037.sje2d.asset.engine.system.IAssetProtocol;
import com.github.yuri6037.sje2d.asset.engine.system.ITypeRegistry;
import com.github.yuri6037.sje2d.asset.engine.system.stream.IAssetStream;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.Set;
import java.util.concurrent.Callable;

class AssetLoadTask implements Callable<AssetLoadTask> {
    private static final Logger LOGGER = LoggerFactory.getLogger(AssetLoadTask.class);
    private final ITypeRegistry registry;
    private final AssetMap map;
    private AssetURL url;
    private IAssetLoader loader = null;
    private int ttl = Constants.MAX_ITERATIONS;
    private IAssetLoader.Result lastResult = null;
    private final AssetDepMap deps = new AssetDepMap();

    public record Result(IAssetLoader loader, Set<String> deps) {
    }

    AssetLoadTask(final ITypeRegistry registry, final AssetMap map, final AssetURL url) {
        this.registry = registry;
        this.map = map;
        this.url = url;
    }

    public Result tryFinish() {
        if (loader == null || lastResult == null || !lastResult.isReady()) {
            return null;
        }
        return new Result(loader, deps.toSet());
    }

    public boolean isAlive() {
        return loader != null && ttl > 0;
    }

    private IAssetFactory getFactory(final AssetURL url1) {
        IAssetFactory factory = registry.getFactory(url1.getMimeType());
        if (factory != null) {
            return factory;
        } else {
            try {
                return registry.getFactory(url1.guessAssetType().toLowerCase() + "/*");
            } catch (MalformedURLException e) {
                LOGGER.warn("Failed to guess asset type");
                return null;
            }
        }
    }

    private boolean init() {
        LOGGER.info("Initializing asset loader for '{}'", url);
        IAssetProtocol protocol = registry.getProtocol(url.getProtocol());
        if (protocol == null) {
            LOGGER.error("Unknown protocol '{}'", url.getProtocol());
            return false;
        }
        LOGGER.debug("Using protocol {}", protocol.getClass().getName());
        IAssetStream stream = null;
        try {
            stream = protocol.open(url);
            if (stream == null) {
                LOGGER.error("The protocol does not support the given URL");
                return false;
            }
            IAssetFactory factory;
            if (url.getMimeType() == null) {
                if (!protocol.canProvideMimeType()) {
                    LOGGER.error("The protocol '{}' does not support asset mime-type inference", url.getProtocol());
                    return false;
                }
                String mimeType = stream.getMimeType();
                if (mimeType == null) {
                    LOGGER.error("Failed to infer mime-type for url '{}'", url);
                    return false;
                }
                url = url.withMimeType(mimeType);
            }
            factory = getFactory(url);
            if (factory == null) {
                LOGGER.error("Unknown asset mime-type '{}'", url.getMimeType());
                return false;
            }
            LOGGER.debug("Using factory {}", factory.getClass().getName());
            loader = factory.create(stream, url);
            if (loader == null) {
                LOGGER.error("Unsupported create operation in factory '{}'", factory.getClass().getName());
                return false;
            }
            LOGGER.debug("Using loader {}", loader.getClass().getName());
            return true;
        } catch (Exception e) {
            LOGGER.error("Failed to create and/or initialize an asset loader", e);
            return false;
        } finally {
            if (stream != null) {
                try {
                    stream.close();
                } catch (IOException e) {
                    LOGGER.warn("Failed to close asset stream", e);
                }
            }
        }
    }

    private void step() throws Exception {
        if (lastResult == null) {
            lastResult = loader.load(deps);
        }
        if (!lastResult.isReady()) {
            Thread.sleep(Constants.ITERATION_WAIT_MILLIS);
            for (String dep : lastResult.getDependencies()) {
                if (!deps.contains(dep)) {
                    AssetStore<?> store = map.lock(dep);
                    if (store == null) {
                        LOGGER.warn("Dependency '{}' not found at iteration {}", dep,
                                (Constants.MAX_ITERATIONS - ttl) + 1);
                        return;
                    }
                    deps.put(store);
                }
            }
            lastResult = null;
        }
    }

    @Override
    public AssetLoadTask call() {
        if (loader == null) {
            if (!init()) {
                loader = null;
            }
        }
        if (loader != null) {
            try {
                step();
                --ttl;
            } catch (Exception e) {
                LOGGER.error("Failed to load asset '{}'", url, e);
                loader = null;
            }
        }
        return this;
    }

    @Override
    public String toString() {
        return url.toString();
    }
}
