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

package com.github.yuri6037.sje2d.ui.core.render.primitive;

import com.github.yuri6037.sje2d.render.Point;
import com.github.yuri6037.sje2d.render.Size;
import com.github.yuri6037.sje2d.ui.asset.style.RectangleStyle;
import com.github.yuri6037.sje2d.ui.core.render.IRender;

/**
 * A rectangle primitive.
 */
public final class Rectangle {
    private RectangleStyle style;

    /**
     * @return the style of this rectangle primitive.
     */
    public RectangleStyle getStyle() {
        return style;
    }

    /**
     * Sets the style of this primitive.
     * @param style the new style.
     */
    public void setStyle(final RectangleStyle style) {
        this.style = style;
    }

    /**
     * Draws this primitive matching the requested style.
     * @param render an instance of the UI rendering engine.
     * @param pos the position of the rectangle.
     * @param size the size of the rectangle.
     */
    public void draw(final IRender render, final Point pos, final Size size) {
        draw(render, pos.x(), pos.y(), size.width(), size.height());
    }

    /**
     * Draws this primitive matching the requested style.
     * @param render an instance of the UI rendering engine.
     * @param x the X coordinate of the rectangle.
     * @param y the Y coordinate of the rectangle.
     * @param width the rectangle width.
     * @param height the rectangle height.
     */
    public void draw(final IRender render, final float x, final float y, final float width, final float height) {
        render.setColor(style.getColor());
        render.setTexture(style.getTexture());
        if (style.getBorderRadius() > 0) {
            render.drawRoundedRect(x, y, width, height, style.getBorderRadius());
        } else {
            render.drawRect(x, y, width, height);
        }
        if (style.hasBorder()) {
            render.setColor(style.getBorderColor());
            render.setTexture(style.getBorderTexture());
            render.drawOutlineRect(x, y, width, height, style.getBorderSize());
        }
    }
}
