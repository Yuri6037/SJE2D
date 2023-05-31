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

public class AssetStore<T extends IAsset> {
    private final String vpath;

    private int uses;

    private final T asset;

    /**
     * Creates a new asset store.
     * @param vpath the virtual path of the asset.
     * @param asset the asset object instance.
     */
    public AssetStore(final String vpath, final T asset) {
        this.vpath = vpath;
        this.asset = asset;
        uses = 0;
    }

    /**
     * @return the asset object instance.
     */
    public T getAsset() {
        return asset;
    }

    /**
     * The use count does not track dependencies or dependents of the asset but tracks the number of references
     * of this asset in the application.
     * @return the number of uses of the asset.
     */
    public int getUses() {
        return uses;
    }

    /**
     * @return the virtual path of this asset.
     */
    public String getVirtualPath() {
        return vpath;
    }

    public class Ref implements AutoCloseable {
        /**
         * Creates a new reference linked to this store and increments the number of uses.
         */
        public Ref() {
            uses += 1;
        }

        /**
         * @return the asset object instance.
         */
        public final T get() {
            return asset;
        }

        /**
         * Closes this reference and decrements the number of uses.
         */
        @Override
        public void close() {
            uses -= 1;
        }

        /**
         * Clones this reference and increment the number of uses.
         * @return a new reference.
         */
        public Ref newRef() {
            return new Ref();
        }
    }
}
