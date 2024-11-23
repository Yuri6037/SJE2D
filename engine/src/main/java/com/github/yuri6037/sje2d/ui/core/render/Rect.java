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

package com.github.yuri6037.sje2d.ui.core.render;

import com.github.yuri6037.sje2d.render.Point;
import com.github.yuri6037.sje2d.render.Size;

/**
 * An extended Rect class which guarantees that the same memory will be used to store
 * the rectangle event after changing the size or the position of it.
 */
public final class Rect {
    private Point pos;
    private Size size;

    /**
     * Creates a new instance of a Rect.
     * @param pos the position in 2D space of the rectangle.
     * @param size the size of the rectangle.
     */
    public Rect(final Point pos, final Size size) {
        this.pos = pos;
        this.size = size;
    }

    /**
     * Creates a new zero rectangle.
     */
    public Rect() {
        this(Point.ZERO, Size.ZERO);
    }

    /**
     * Sets the position of this rectangle.
     * @param pos the new position.
     */
    public void setPos(final Point pos) {
        this.pos = pos;
    }

    /**
     * Sets the size of this rectangle.
     * @param size the new size.
     */
    public void setSize(final Size size) {
        this.size = size;
    }

    /**
     * @return the rectangle position.
     */
    public Point getPos() {
        return pos;
    }

    /**
     * @return the rectangle size.
     */
    public Size getSize() {
        return size;
    }
}
