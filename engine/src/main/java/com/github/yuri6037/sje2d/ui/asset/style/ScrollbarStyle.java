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

@Reflect
public final class ScrollbarStyle extends Style {
    private float size;
    private float minSize;

    /**
     * Creates a new ScrollbarStyle.
     */
    public ScrollbarStyle() {
        addParam("size", Float.class, this::setSize);
        addParam("minSize", Float.class, this::setMinSize);
    }

    /**
     * Sets the width of all scrollbars rendered with this style.
     * @param size1 the new width of the scrollbar.
     * @return this for chaining operations.
     */
    public ScrollbarStyle setSize(final float size1) {
        size = size1;
        return this;
    }

    /**
     * Sets the minimum length of all scrollbars rendered with this style.
     * @param minSize1 the new minimum length of the scrollbar.
     * @return this for chaining operations.
     */
    public ScrollbarStyle setMinSize(final float minSize1) {
        minSize = minSize1;
        return this;
    }

    /**
     * @return the width of a scrollbar matching this style.
     */
    public float getSize() {
        return size;
    }

    /**
     * @return the minimum length of a scrollbar matching this style.
     */
    public float getMinSize() {
        return minSize;
    }
}
