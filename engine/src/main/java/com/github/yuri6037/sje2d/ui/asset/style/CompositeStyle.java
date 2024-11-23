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

import com.github.yuri6037.sje2d.reflect.Reflect;

import java.util.HashMap;

/**
 * A CompositeStyle is a style containing multiple embedded styles.
 */
@Reflect
public final class CompositeStyle extends Style {
    private final HashMap<String, Style> styles = new HashMap<>();

    /**
     * Adds a new sub-style to this CompositeStyle.
     * @param name the name of the style to add.
     * @param style the style to add.
     * @return this for chaining operations.
     */
    public CompositeStyle add(final String name, final Style style) {
        styles.put(name, style);
        return this;
    }

    /**
     * Finds a sub-style by its name.
     * @param styleClass the class of the style.
     * @param name the name of the embedded style to get.
     * @return an instance of the matching style or null if not found.
     * @param <T> the generic type of the style.
     */
    public <T> T get(final Class<T> styleClass, final String name) {
        if (!styles.containsKey(name)) {
            return null;
        }
        return styleClass.cast(styles.get(name));
    }
}
