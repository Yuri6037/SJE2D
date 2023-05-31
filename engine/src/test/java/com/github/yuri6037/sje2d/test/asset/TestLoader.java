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

package com.github.yuri6037.sje2d.test.asset;

import com.github.yuri6037.sje2d.asset.engine.AssetURL;
import com.github.yuri6037.sje2d.asset.engine.map.AssetDepMap;
import com.github.yuri6037.sje2d.asset.engine.map.AssetStore;
import com.github.yuri6037.sje2d.asset.engine.system.IAssetFactory;
import com.github.yuri6037.sje2d.asset.engine.system.IAssetLoader;
import com.github.yuri6037.sje2d.asset.engine.system.ITAssetLoader;
import com.github.yuri6037.sje2d.asset.engine.system.stream.IAssetStream;

/**
 * An asset loader for UT.
 * URL Parameters:
 *      - dep: the virtual path of an optional dependency.
 *      - sleep: the sleep delay before and after checking the dependency in milliseconds.
 *      - vpath: the virtual path of the com.github.yuri6037.minengine.test.asset once loaded.
 *      - uthrow: false if the asset should not throw when unloading.
 */
public final class TestLoader implements ITAssetLoader<Test> {
    public static final class Factory implements IAssetFactory {
        @Override
        public IAssetLoader create(final IAssetStream stream, final AssetURL url) {
            return new TestLoader(url);
        }

        @Override
        public String getMimeType() {
            return "test/test";
        }
    }

    private final AssetURL url;
    private boolean shouldThrow = true;

    private TestLoader(final AssetURL url) {
        this.url = url;
    }

    @Override
    public Result load(final AssetDepMap dependencies) throws Exception {
        String dep = url.getParameter("dep");
        String sleepstr = url.getParameter("sleep");
        String uthrow = url.getParameter("uthrow");
        if (uthrow != null && uthrow.equals("false")) {
            shouldThrow = false;
        }
        int sleep = Integer.parseInt(sleepstr == null ? "0" : sleepstr);
        if (sleep > 0) {
            Thread.sleep(sleep);
        }
        if (dep != null) {
            Test test = dependencies.get(Test.class, dep);
            if (test == null) {
                return Result.needsDependencies(new String[]{dep});
            }
        }
        if (sleep > 0) {
            Thread.sleep(sleep);
        }
        return Result.ready();
    }

    @Override
    public AssetStore<Test> create() {
        String vpath = url.getParameter("name");
        return new AssetStore<>(vpath, new Test(url.getPath(), shouldThrow));
    }
}
