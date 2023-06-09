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

 package com.github.yuri6037.sje2d.test;

import com.github.yuri6037.sje2d.asset.config.TypeRegistry;
import com.github.yuri6037.sje2d.asset.engine.manager.AssetManager;
import com.github.yuri6037.sje2d.asset.engine.manager.AssetManagerProxy;
import com.github.yuri6037.sje2d.test.asset.TestLoader;
import com.github.yuri6037.sje2d.test.asset.TestProtocol;

public abstract class TestAssetsBase {
    //CHECKSTYLE OFF: VisibilityModifier
    /**
     * A reference to the UT assets manager.
     */
    protected final AssetManager manager;

    /**
     * A reference to the UT assets manager proxy.
     */
    protected final AssetManagerProxy proxy;
    //CHECKSTYLE ON

    /**
     * Creates a new UT module for the asset system.
     * @throws Exception if the setup function failed.
     */
    public TestAssetsBase() throws Exception {
        TypeRegistry registry = getRegistryBuilder().build();
        manager = new AssetManager(registry);
        proxy = manager.newProxy();
        setup();
    }

    /**
     * Creates the registry builder. Override this method to customize the asset registry.
     * @return the new registry builder.
     */
    protected TypeRegistry.Builder getRegistryBuilder() {
        return new TypeRegistry.Builder()
                .addFactory(new TestLoader.Factory())
                .addProtocol(new TestProtocol("test/test"));
    }

    /**
     * Function called when initializing the UT module. Add initial assets to load in here.
     * @throws Exception if the initialization of the UT module failed.
     */
    protected abstract void setup() throws Exception;
}
