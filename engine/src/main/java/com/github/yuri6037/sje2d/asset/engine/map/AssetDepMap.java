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

package com.github.yuri6037.sje2d.asset.engine.map;

import com.github.yuri6037.sje2d.asset.engine.system.IAsset;

import java.util.HashMap;
import java.util.Set;

/**
 * This class represents a sub-set of assets which are expected to be
 * dependencies of a particular asset.
 */
public class AssetDepMap {
    private final HashMap<String, AssetStore<? extends IAsset>> map = new HashMap<>();

    /**
     * Inserts a new asset in this dependency map.
     * @param asset the new asset store to insert.
     * @param <T> the type of the stored asset.
     */
    public <T extends IAsset> void put(final AssetStore<T> asset) {
        map.put(asset.getVirtualPath(), asset);
    }

    /**
     * Gets an asset from this map.
     * @param c the asset class, this is required because the java compiler has a defect.
     * @param vpath virtual path of the asset.
     * @return an instance of an asset cast as T or null if not possible.
     * @param <T> the type of the asset to get.
     */
    public <T extends IAsset> T get(final Class<T> c, final String vpath) {
        AssetStore<?> store = map.get(vpath);
        if (store == null) {
            return null;
        }
        try {
            return c.cast(store.getAsset());
        } catch (ClassCastException e) {
            return null;
        }
    }

    /**
     * Returns true if an asset is present in this dependency map.
     * @param vpath the virtual path of the asset.
     * @return true if the asset exists, false otherwise.
     */
    public boolean contains(final String vpath) {
        return map.containsKey(vpath);
    }

    /**
     * @return a set of all assets (virtual path) present in this dependency map.
     */
    public Set<String> toSet() {
        return map.keySet();
    }
}
