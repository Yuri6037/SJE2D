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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

public class ClassRegistry {
    private static final Logger LOGGER = LoggerFactory.getLogger(ClassRegistry.class);

    private static final HashMap<String, Class<? extends IConfigurable>> classes = new HashMap<>();
    private static final ArrayList<String> searchPaths = new ArrayList<>();

    public static void add(final Class<? extends IConfigurable> cl) {
        if (cl.getAnnotation(Reflect.class) == null) {
            LOGGER.error("Not adding class {}: class is not Layout aware", cl);
            return;
        }
        classes.put(cl.getSimpleName(), cl);
    }

    public static void addSearchPath(final String path) {
        searchPaths.add(path);
    }

    public static Class<? extends IConfigurable> getClass(final String name) throws ClassNotFoundException {
        Class<? extends IConfigurable> cl = classes.get(name);
        if (cl != null)
            return cl;
        for (String path: searchPaths) {
            try {
                LOGGER.debug("{}.{}", path, name);
                Class<?> cl1 = Class.forName(path + "." + name);
                LOGGER.debug("{}", cl1);
                if (cl1.getAnnotation(Reflect.class) != null && IConfigurable.class.isAssignableFrom(cl1)) {
                    //noinspection unchecked
                    return (Class<? extends IConfigurable>) cl1;
                }
            } catch (ClassNotFoundException ignored) {
                //This is ignored because we throw it at the end.
            }
        }
        throw new ClassNotFoundException("Could not find component class for name " + name);
    }
}
