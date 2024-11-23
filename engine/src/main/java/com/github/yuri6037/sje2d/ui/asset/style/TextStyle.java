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

import com.github.yuri6037.sje2d.asset.Font;
import com.github.yuri6037.sje2d.reflect.Reflect;
import com.github.yuri6037.sje2d.render.Color;

@Reflect
public final class TextStyle extends Style {
    private Font font;

    private Color color;

    /**
     * Creates a new TextStyle.
     */
    public TextStyle() {
        addParam("color", Color.class, this::setColor);
        addParam("font", Font.class, this::setFont);
    }

    /**
     * Sets the color of text rendered with this style.
     * @param color1 the new color.
     * @return this for chaining operations.
     */
    public TextStyle setColor(final Color color1) {
        color = color1;
        return this;
    }

    /**
     * Sets the font of text rendered with this style.
     * @param font1 the new color.
     * @return this for chaining operations.
     */
    public TextStyle setFont(final Font font1) {
        font = font1;
        return this;
    }

    /**
     * @return the font of text rendered with this style.
     */
    public Font getFont() {
        return font;
    }

    /**
     * @return the color of text rendered with this style.
     */
    public Color getColor() {
        return color;
    }
}
