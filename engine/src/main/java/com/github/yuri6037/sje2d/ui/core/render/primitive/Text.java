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

import com.github.yuri6037.sje2d.asset.Font;
import com.github.yuri6037.sje2d.render.Color;
import com.github.yuri6037.sje2d.render.Point;
import com.github.yuri6037.sje2d.render.Size;
import com.github.yuri6037.sje2d.ui.asset.style.TextStyle;
import com.github.yuri6037.sje2d.ui.core.render.IRender;
import com.github.yuri6037.sje2d.util.UTF32Str;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.UnsupportedEncodingException;

/**
 * A standard text primitive.
 */
public class Text {
    private static final Logger LOGGER = LoggerFactory.getLogger(Text.class);

    private UTF32Str text;
    private TextStyle style;
    private Color color;
    private Font font;

    /**
     * @return the color of the text to be rendered..
     */
    public Color getColor() {
        return color;
    }

    /**
     * @return the text to be rendered.
     */
    public UTF32Str getText() {
        return text;
    }

    /**
     * @return the font of the text to be rendered.
     */
    public Font getFont() {
        return font;
    }

    /**
     * @return the style of this text primitive.
     */
    public TextStyle getStyle() {
        return style;
    }

    /**
     * Sets the style of this primitive.
     * @param style the new style.
     */
    public final void setStyle(final TextStyle style) {
        this.style = style;
    }

    /**
     * Sets the UTF-32 text of this primitive.
     * @param text the new text.
     */
    public final void setText(final UTF32Str text) {
        this.text = text;
    }

    /**
     * Sets the color of this primitive.
     * @param color the new color.
     */
    public final void setColor(final Color color) {
        this.color = color;
    }

    /**
     * Sets the font of this primitive.
     * @param font the new font.
     */
    public final void setFont(final Font font) {
        this.font = font;
    }

    /**
     * Sets the text of this primitive after converting it to UTF-32.
     * @param text the new text.
     */
    public final void setText(final String text) {
        try {
            this.text = new UTF32Str(text);
        } catch (UnsupportedEncodingException e) {
            LOGGER.error("Failed to get UTF-32 from string", e);
            this.text = null;
        }
    }

    /**
     * Draws this primitive matching the requested style.
     * @param render an instance of the UI rendering engine.
     * @param pos the position of the text.
     */
    public final void draw(final IRender render, final Point pos) {
        draw(render, pos.x(), pos.y());
    }

    /**
     * Draws this primitive matching the requested style.
     * @param render an instance of the UI rendering engine.
     * @param x the X coordinate of the text.
     * @param y the Y coordinate of the text.
     */
    public void draw(final IRender render, final float x, final float y) {
        if (text == null) {
            return;
        }
        render.setFont(font != null ? font : style.getFont());
        render.setColor(color != null ? color : style.getColor());
        render.drawText(text, x, y);
    }

    /**
     * Computes the size of this text primitive matching the requested style.
     * @param render an instance of the UI rendering engine.
     * @return the size of the text.
     */
    public final Size getSze(final IRender render) {
        if (text == null) {
            return Size.ZERO;
        }
        render.setFont(font != null ? font : style.getFont());
        return render.getTextSize(text);
    }
}