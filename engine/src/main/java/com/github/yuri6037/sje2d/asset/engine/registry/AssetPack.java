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

package com.github.yuri6037.sje2d.asset.engine.registry;

import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.yuri6037.sje2d.asset.engine.AssetURLBuilder;
import com.github.yuri6037.sje2d.asset.engine.manager.AssetManagerProxy;

public abstract class AssetPack {
    private static final Logger LOGGER = LoggerFactory.getLogger(AssetPack.class);

    /**
     * Load all asset URLs contained in this pack.
     * @param urls the output list of all URLs to pass to the AssetManagerProxy.
     */
    protected abstract void loadUrls(List<AssetURLBuilder> urls) throws Exception;

    /**
     * Queue all assets that are part of this pack of assets.
     * @param namespace the namespace to mount all assets under, or null if assets should not be namespaced.
     * @param proxy the target AssetManagerProxy to submit new URLs to.
     */
    public void queue(final String namespace, final AssetManagerProxy proxy) {
        ArrayList<AssetURLBuilder> builders = new ArrayList<>();
        try {
            loadUrls(builders);
        } catch (Exception e) {
            LOGGER.error("Failed to load AssetPack", e);
        }
        for (AssetURLBuilder builder : builders) {
            if (namespace != null) {
                builder.parameter("namespace", namespace);
            }
            try {
                proxy.queue(builder.build());
            } catch (MalformedURLException e) {
                LOGGER.warn("Failed to queue asset from AssetPack", e);
            }
        }
    }
}
