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

package com.github.yuri6037.sje2d.reflect;

import com.github.yuri6037.sje2d.render.Color;

import java.util.HashMap;

/**
 * A helper class to implement the IConfigurable interface.
 */
public final class Configurator implements IConfigurable {
    private static class Function<V> {
        //CHECKSTYLE OFF: VisibilityModifier
        IConfigFunction<V> function;
        Class<V> parameterClass;
        //CHECKSTYLE ON

        void apply(final Object value) {
            function.apply(parameterClass.cast(value));
        }
    }

    private final HashMap<String, Function<?>> configMap = new HashMap<>();

    @Override
    public void setParam(final String key, final Object value) throws IllegalArgumentException {
        if (configMap.containsKey(key)) {
            Function<?> f = configMap.get(key);
            if (!value.getClass().isAssignableFrom(f.parameterClass)) {
                throw new IllegalArgumentException("Invalid parameter value for key: " + key);
            }
            f.apply(value);
        } else {
            throw new IllegalArgumentException("Unknown configuration parameter: " + key);
        }
    }

    @Override
    public Class<?> getParamType(final String key) {
        return configMap.containsKey(key) ? configMap.get(key).parameterClass : null;
    }

    /**
     * Parse a primitive parameter.
     * @param paramType the desired parameter type.
     * @param value the parameter value as a string to parse.
     * @return the parsed parameter value or null if the parameter type is unknown to this function.
     */
    public static Object parsePrimitive(final Class<?> paramType, final String value) {
        if (paramType == Boolean.class) {
            return Boolean.parseBoolean(value);
        } else if (paramType == Integer.class) {
            return Integer.parseInt(value);
        } else if (paramType == Float.class) {
            return Float.parseFloat(value);
        } else if (paramType == Double.class) {
            return Double.parseDouble(value);
        } else if (paramType == String.class) {
            return value;
        } else if (paramType == Color.class) {
            return Color.parseColor(value);
        } else if (paramType == Float[].class) {
            String[] values = value.split(",");
            Float[] vals = new Float[values.length];
            for (int i = 0; i != values.length; ++i) {
                vals[i] = Float.parseFloat(values[i]);
            }
            return vals;
        } else if (paramType == Double[].class) {
            String[] values = value.split(",");
            Double[] vals = new Double[values.length];
            for (int i = 0; i != values.length; ++i) {
                vals[i] = Double.parseDouble(values[i]);
            }
            return vals;
        } else if (paramType == Integer[].class) {
            String[] values = value.split(",");
            Integer[] vals = new Integer[values.length];
            for (int i = 0; i != values.length; ++i) {
                vals[i] = Integer.parseInt(values[i]);
            }
            return vals;
        } else if (paramType == Boolean[].class) {
            String[] values = value.split(",");
            Boolean[] vals = new Boolean[values.length];
            for (int i = 0; i != values.length; ++i) {
                vals[i] = Boolean.parseBoolean(values[i]);
            }
            return vals;
        }
        return null;
    }

    /**
     * Adds a configuration parameter.
     * @param key the parameter name.
     * @param parameterClass the type of the parameter.
     * @param function the setter function to call to set the parameter value.
     * @param <V> the generic type of the parameter.
     */
    public <V> void addParam(final String key, final Class<V> parameterClass, final IConfigFunction<V> function) {
        Function<V> f = new Function<>();
        f.parameterClass = parameterClass;
        f.function = function;
        configMap.put(key, f);
    }
}
