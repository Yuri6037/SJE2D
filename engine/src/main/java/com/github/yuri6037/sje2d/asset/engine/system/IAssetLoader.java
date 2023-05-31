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

package com.github.yuri6037.sje2d.asset.engine.system;

import com.github.yuri6037.sje2d.asset.engine.map.AssetDepMap;
import com.github.yuri6037.sje2d.asset.engine.map.AssetStore;

public interface IAssetLoader {
    class Result {
        private final String[] dependencies;
        private final boolean isFinished;
        private final boolean isNone;

        private Result(final boolean isFinished, final boolean isNone, final String[] dependencies) {
            this.dependencies = dependencies;
            this.isFinished = isFinished;
            this.isNone = isNone;
        }

        private static final Result READY = new Result(true, false, null);
        private static final Result NONE = new Result(true, true, null);

        /**
         * Creates a new result to signal a lack of dependency to continue.
         * @param dependencies the dependencies needed to continue loading.
         * @return the new result.
         */
        public static Result needsDependencies(final String[] dependencies) {
            return new Result(false, false, dependencies);
        }

        /**
         * Creates a new result which ends loading.
         * @return a new result.
         */
        public static Result ready() {
            return READY;
        }

        /**
         * Creates a new result which ends loading but indicates to the solver that this loader does not produce assets.
         * @return a new result.
         */
        public static Result none() {
            return NONE;
        }

        /**
         * @return true if the loader is finished, false otherwise
         */
        public boolean isReady() {
            return isFinished;
        }

        /**
         * @return true if the loader will not produce any asset, false otherwise
         */
        public boolean isNone() {
            return isNone;
        }

        /**
         * @return the dependencies needed to continue.
         */
        public final String[] getDependencies() {
            return dependencies;
        }
    }

    /**
     * Function called in a thread used to do all the IO work and required processing to use it.
     * In this context, all platform APIs (OpenGL, OpenAL, etc) are unavailable.
     * @param dependencies the dependencies required when loading this asset, initially null until some dependencies
     *                     are needed.
     * @return Result.ready if this is asset is ready to be created and presented, Result.needsDependencies if this
     *                     asset still needs more dependencies.
     * @throws Exception if an unrecoverable exception occurs while loading the asset.
     */
    Result load(AssetDepMap dependencies) throws Exception;

    /**
     * Function called in the main thread to create the final asset.
     * This is the point where any platform (OpenGL, OpenAL, etc) calls are safe to perform.
     * @return the newly created asset.
     * @throws Exception if an unrecoverable exception occurs while creating the asset
     * (ex: low-level render driver failed to allocate or returned an error code).
     */
     AssetStore<?> create() throws Exception;
}
