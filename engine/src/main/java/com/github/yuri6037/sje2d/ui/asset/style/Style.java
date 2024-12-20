/*
 * Copyright (c) 2024, SJE2D
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

package com.github.yuri6037.sje2d.ui.asset.style;

import com.github.yuri6037.sje2d.asset.engine.system.IAsset;
import com.github.yuri6037.sje2d.reflect.Configurator;
import com.github.yuri6037.sje2d.reflect.IConfigFunction;
import com.github.yuri6037.sje2d.reflect.IConfigurable;

/**
 * The base asset class for all types of styles.
 */
public abstract class Style implements IConfigurable, IAsset {
    private final Configurator configurator = new Configurator();

    @Override
    public final void setParam(final String key, final Object value) throws IllegalArgumentException {
        configurator.setParam(key, value);
    }

    @Override
    public final Class<?> getParamType(final String key) {
        return configurator.getParamType(key);
    }

    /**
     * Adds a configuration parameter to this style class.
     * @param key the name of the parameter to be configured.
     * @param parameterClass the type class of the parameter value.
     * @param function the set function to bind to the parameter.
     * @param <V> the generic type of the parameter value.
     */
    protected final <V> void addParam(final String key, final Class<V> parameterClass,
                                      final IConfigFunction<V> function) {
        configurator.addParam(key, parameterClass, function);
    }

    @Override
    public void unload() {
    }
}
