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

import com.github.yuri6037.sje2d.asset.Texture;
import com.github.yuri6037.sje2d.render.Color;
import com.github.yuri6037.sje2d.reflect.Reflect;

@Reflect
public final class RectangleStyle extends Style {
    private Color color;
    private Texture texture;

    private boolean hasBorder;
    private Color borderColor;
    private Texture borderTexture;
    private float borderSize;

    /**
     * Creates a new instance of a RectangleStyle.
     */
    public RectangleStyle() {
        addParam("color", Color.class, this::setColor);
        addParam("texture", Texture.class, this::setTexture);
        addParam("hasBorder", Boolean.class, this::setHasBorder);
        addParam("borderColor", Color.class, this::setBorderColor);
        addParam("borderTexture", Texture.class, this::setBorderTexture);
        addParam("borderSize", Float.class, this::setBorderSize);
    }

    /**
     * Sets the fill color of this rectangle style.
     * @param color1 the new color.
     * @return this for chaining operations.
     */
    public RectangleStyle setColor(final Color color1) {
        color = color1;
        return this;
    }

    /**
     * Sets the fill texture of this rectangle style.
     * @param texture1 the new texture.
     * @return this for chaining operations.
     */
    public RectangleStyle setTexture(final Texture texture1) {
        texture = texture1;
        return this;
    }

    /**
     * Sets whether this rectangle style should render a border.
     * @param hasBorder1 true to draw a border, false otherwise.
     * @return this for chaining operations.
     */
    public RectangleStyle setHasBorder(final boolean hasBorder1) {
        hasBorder = hasBorder1;
        return this;
    }

    /**
     * Sets the border color of this rectangle style.
     * @param color1 the new color.
     * @return this for chaining operations.
     */
    public RectangleStyle setBorderColor(final Color color1) {
        borderColor = color1;
        return this;
    }

    /**
     * Sets the border texture of this rectangle style.
     * @param texture1 the new texture.
     * @return this for chaining operations.
     */
    public RectangleStyle setBorderTexture(final Texture texture1) {
        borderTexture = texture1;
        return this;
    }

    /**
     * Sets the border size of this rectangle style.
     * @param width the new border width.
     * @return this for chaining operations.
     */
    public RectangleStyle setBorderSize(final float width) {
        borderSize = width;
        return this;
    }

    /**
     * @return the fill color of a rectangle matching this style.
     */
    public Color getColor() {
        return color;
    }

    /**
     * @return the fill texture of a rectangle matching this style.
     */
    public Texture getTexture() {
        return texture;
    }

    /**
     * @return the border color of a rectangle matching this style.
     */
    public Color getBorderColor() {
        return borderColor;
    }

    /**
     * @return the border texture of a rectangle matching this style.
     */
    public Texture getBorderTexture() {
        return borderTexture;
    }

    /**
     * @return the border width of a rectangle matching this style.
     */
    public float getBorderSize() {
        return borderSize;
    }

    /**
     * @return true if rectangles matching this style should have a border, false otherwise.
     */
    public boolean hasBorder() {
        return hasBorder;
    }
}
