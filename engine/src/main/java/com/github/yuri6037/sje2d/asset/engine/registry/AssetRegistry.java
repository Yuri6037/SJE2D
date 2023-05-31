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

import java.util.ArrayList;

import com.github.yuri6037.sje2d.asset.engine.manager.AssetManagerProxy;

public final class AssetRegistry {
    private final ArrayList<String> namespaces = new ArrayList<>();
    private final ArrayList<String> assets = new ArrayList<>();

    /**
     * Put an asset namespace into this registry.
     * @param namespace the asset namespace.
     */
    public void putNamespace(final String namespace) {
        namespaces.add(namespace);
    }

    /**
     * Puts a single asset into this registry.
     * @param vpath the virtual path of the asset.
     */
    public void putAsset(final String vpath) {
        assets.add(vpath);
    }

    /**
     * Unloads this asset registry.
     * @param proxy the target AssetManagerProxy.
     * @return the number of operations queued.
     */
    public int unload(final AssetManagerProxy proxy) {
        int lastOps = proxy.getOperationCount();
        int ops = 0;
        for (String namespace: namespaces) {
            ops = proxy.unloadNamespace(namespace);
        }
        for (String vpath: assets) {
            ops = proxy.unload(vpath);
        }
        return ops - lastOps < 0 ? 0 : ops - lastOps;
    }
}
