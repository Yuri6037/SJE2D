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

import com.github.yuri6037.sje2d.asset.engine.AssetURL;
import org.junit.Assert;
import org.junit.Test;

public final class TestAssetsDep extends TestAssetsBase {
    /**
     * Creates a new UT module for the asset system.
     * @throws Exception if the setup function failed.
     */
    public TestAssetsDep() throws Exception {
        super();
    }

    @Override
    protected void setup() {
    }

    /**
     * Test if asset dependencies are correctly solved when assets are added in inverse order of dependency
     * (ie. from the fewest deps to the largest deps).
     * @throws Exception if some assets manager operation failed.
     */
    @Test
    public void ordered() throws Exception {
        proxy.queue(new AssetURL("test/test", "test", "this is a test?name=basic"));
        manager.waitAll();
        Assert.assertEquals(1, proxy.getAssetsCount());
        proxy.queue(new AssetURL("test/test", "test", "this is a test?name=basic1&dep=basic"));
        manager.waitAll();
        Assert.assertEquals(2, proxy.getAssetsCount());
    }

    /**
     * Test if asset dependencies are correctly solved when assets are added in order of dependency
     * (ie. from the largest deps to the fewest deps).
     * @throws Exception if some assets manager operation failed.
     */
    @Test
    public void unordered() throws Exception {
        proxy.queue(new AssetURL("test/test", "test", "this is a test?name=basic1&dep=basic"));
        proxy.queue(new AssetURL("test/test", "test", "this is a test?name=basic"));
        manager.waitAll();
        Assert.assertEquals(2, proxy.getAssetsCount());
    }

    /**
     * Test if asset dependencies are correctly solved when more assets than available threads (4) are added in
     * inverse order of dependency (ie. from the fewest deps to the largest deps).
     * @throws Exception if some assets manager operation failed.
     */
    @Test
    public void orderedOverload() throws Exception {
        proxy.queue(new AssetURL("test/test", "test", "this is a test?name=basic"));
        proxy.queue(new AssetURL("test/test", "test", "this is a test?name=basic1&dep=basic"));
        proxy.queue(new AssetURL("test/test", "test", "this is a test?name=basic2&dep=basic"));
        proxy.queue(new AssetURL("test/test", "test", "this is a test?name=basic3&dep=basic"));
        proxy.queue(new AssetURL("test/test", "test", "this is a test?name=basic4&dep=basic"));
        manager.waitAll();
        Assert.assertEquals(5, proxy.getAssetsCount());
    }

    /**
     * Test if asset dependencies are correctly solved when more assets than available threads (4) are added in
     * order of dependency (ie. from the largest deps to the fewest deps).
     * @throws Exception if some assets manager operation failed.
     */
    @Test
    public void unorderedOverload() throws Exception {
        proxy.queue(new AssetURL("test/test", "test", "this is a test?name=basic1&dep=basic"));
        proxy.queue(new AssetURL("test/test", "test", "this is a test?name=basic2&dep=basic"));
        proxy.queue(new AssetURL("test/test", "test", "this is a test?name=basic3&dep=basic"));
        proxy.queue(new AssetURL("test/test", "test", "this is a test?name=basic4&dep=basic"));
        proxy.queue(new AssetURL("test/test", "test", "this is a test?name=basic"));
        manager.waitAll();
        Assert.assertEquals(5, proxy.getAssetsCount());
    }

    /**
     * Test if asset dependencies are correctly solved when 100 (99 dependents + 1 dependency) assets are added in
     * order of dependency (ie. from the largest deps to the fewest deps).
     * @throws Exception if some assets manager operation failed.
     */
    @Test
    public void unorderedOverload2() throws Exception {
        for (int i = 1; i != 100; ++i) {
            proxy.queue(new AssetURL("test/test", "test", "this is a test?name=basic" + i + "&dep=basic"));
        }
        proxy.queue(new AssetURL("test/test", "test", "this is a test?name=basic"));
        manager.waitAll();
        Assert.assertEquals(100, proxy.getAssetsCount());
    }

    /**
     * Test if asset dependencies are correctly solved when assets are added and synced in order of dependency
     * (ie. from the largest deps to the fewest deps).
     * @throws Exception if some assets manager operation failed.
     */
    @Test
    public void unordered2() throws Exception {
        proxy.queue(new AssetURL("test/test", "test", "this is a test?name=basic1&dep=basic"));
        manager.update();
        proxy.queue(new AssetURL("test/test", "test", "this is a test?name=basic"));
        manager.waitAll();
        Assert.assertEquals(2, proxy.getAssetsCount());
    }

    /**
     * Test if assets are correctly unloaded when the dependent is unloaded before the dependency.
     * @throws Exception if some assets manager operation failed.
     */
    @Test
    public void orderedUnload() throws Exception {
        proxy.queue(new AssetURL("test/test", "test", "this is a test?name=basic"));
        manager.waitAll();
        proxy.queue(new AssetURL("test/test", "test", "this is a test?name=basic1&dep=basic"));
        manager.waitAll();
        Assert.assertEquals(2, proxy.getAssetsCount());
        proxy.unload("basic1");
        Assert.assertThrows(RuntimeException.class, manager::waitAll);
        Assert.assertEquals(1, proxy.getAssetsCount());
        proxy.unload("basic");
        Assert.assertThrows(RuntimeException.class, manager::waitAll);
        Assert.assertEquals(0, proxy.getAssetsCount());
    }

    /**
     * Test if assets are correctly unloaded when the dependency is unloaded first, automatically taking the
     * dependent with it.
     * @throws Exception if some assets manager operation failed.
     */
    @Test
    public void unorderedUnload() throws Exception {
        proxy.queue(new AssetURL("test/test", "test", "this is a test?name=basic&uthrow=false"));
        manager.waitAll();
        proxy.queue(new AssetURL("test/test", "test", "this is a test?name=basic1&dep=basic&uthrow=false"));
        manager.waitAll();
        proxy.unload("basic");
        manager.waitAll();
        Assert.assertEquals(0, proxy.getAssetsCount());
    }
}
