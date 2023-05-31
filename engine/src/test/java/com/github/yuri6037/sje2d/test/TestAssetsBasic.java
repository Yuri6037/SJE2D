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

package com.github.yuri6037.sje2d.test;

import com.github.yuri6037.sje2d.asset.engine.map.AssetStore;
import com.github.yuri6037.sje2d.asset.engine.AssetURL;
import com.github.yuri6037.sje2d.test.asset.Text;
import org.junit.Assert;
import org.junit.Test;

public final class TestAssetsBasic extends TestAssetsBase {
    /**
     * Creates a new UT module for the asset system.
     * @throws Exception if the setup function failed.
     */
    public TestAssetsBasic() throws Exception {
        super();
    }

    @Override
    protected void setup() throws Exception {
        proxy.queue(new AssetURL("test/test", "test", "this is a test?name=basic"));
        manager.waitAll();
    }

    /**
     * Test that the asset is properly loaded and that it exists.
     */
    @Test
    public void load() {
        Assert.assertEquals(0, proxy.getOperationCount());
        Assert.assertNotNull(proxy.get(com.github.yuri6037.sje2d.test.asset.Test.class, "basic"));
    }

    /**
     * Simple unload test with no dependency/dependent involved.
     */
    @Test
    public void unload() {
        Assert.assertFalse(proxy.isInUse("basic"));
        Assert.assertEquals(1, proxy.getAssetsCount());
        Assert.assertEquals(1, proxy.unload("basic"));
        Assert.assertThrows(RuntimeException.class, manager::waitAll);
        Assert.assertEquals(0, proxy.getAssetsCount());
        Assert.assertNull(proxy.get(com.github.yuri6037.sje2d.test.asset.Test.class, "basic"));
    }

    /**
     * Test that unloading an asset is forbidden when that particular asset is currently in use.
     * @throws Exception if some assets manager operation failed.
     */
    @Test
    public void unloadForbid() throws Exception {
        Assert.assertFalse(proxy.isInUse("basic"));
        Assert.assertEquals(1, proxy.getAssetsCount());
        AssetStore<com.github.yuri6037.sje2d.test.asset.Test>.Ref ref
                = proxy.get(com.github.yuri6037.sje2d.test.asset.Test.class, "basic");
        Assert.assertEquals("this is a test", ref.get().getPath());
        Assert.assertTrue(proxy.isInUse("basic"));
        Assert.assertEquals(1, proxy.unload("basic"));
        manager.waitAll();
        Assert.assertEquals(0, proxy.getOperationCount());
        Assert.assertEquals(1, proxy.getAssetsCount());
        Assert.assertNotNull(proxy.get(com.github.yuri6037.sje2d.test.asset.Test.class, "basic"));
    }

    /**
     * Test that the asset proxy is type safe
     * (ie. asset types are checked and do not throw exceptions when types don't match).
     */
    @Test
    public void typeSafety() {
        Assert.assertNotNull(proxy.get(com.github.yuri6037.sje2d.test.asset.Test.class, "basic"));
        Assert.assertNull(proxy.get(Text.class, "basic"));
        Assert.assertNull(proxy.get(Text.class, "nonexistent"));
        Assert.assertNull(proxy.get(com.github.yuri6037.sje2d.test.asset.Test.class, "basic1"));
    }
}
