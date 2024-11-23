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

package com.github.yuri6037.sje2d.ui.component;

import com.github.yuri6037.sje2d.asset.Font;
import com.github.yuri6037.sje2d.reflect.Reflect;
import com.github.yuri6037.sje2d.render.Color;
import com.github.yuri6037.sje2d.render.Size;
import com.github.yuri6037.sje2d.ui.asset.Theme;
import com.github.yuri6037.sje2d.ui.asset.style.TextStyle;
import com.github.yuri6037.sje2d.ui.core.input.IInput;
import com.github.yuri6037.sje2d.ui.core.render.IRender;
import com.github.yuri6037.sje2d.ui.core.render.Rect;
import com.github.yuri6037.sje2d.ui.core.render.primitive.Text;
import com.github.yuri6037.sje2d.util.UTF32Str;

@Reflect
public final class Label extends Component {
    private final Text text = new Text();

    /**
     * Creates a new Label component.
     */
    public Label() {
        addParam("color", Color.class, this::setColor);
        addParam("font", Font.class, this::setFont);
        addParam("text", UTF32Str.class, this::setText);
        addParam("style", TextStyle.class, this::setStyle);
    }

    /**
     * Sets the style of this label.
     * @param style the new style of the label.
     * @return this for chaining operations.
     */
    public Label setStyle(final TextStyle style) {
        text.setStyle(style);
        return this;
    }

    /**
     * Sets the color of this label.
     * @param color the new color of the label.
     * @return this for chaining operations.
     */
    public Label setColor(final Color color) {
        text.setColor(color);
        return this;
    }

    /**
     * Sets the font of this label.
     * @param font the new font of the label.
     * @return this for chaining operations.
     */
    public Label setFont(final Font font) {
        text.setFont(font);
        return this;
    }

    /**
     * Sets the text of this label.
     * @param text1 the new text already in UTF32 format.
     * @return this for chaining operations.
     */
    public Label setText(final UTF32Str text1) {
        this.text.setText(text1);
        return this;
    }

    /**
     * Sets the text of this label.
     * @param text1 the new text as a standard java String to be converted to UTF32.
     * @return this for chaining operations.
     */
    public Label setText(final String text1) {
        this.text.setText(text1);
        return this;
    }

    @Override
    public void update(final Rect parentRect, final IRender render, final IInput input, final Rect rect) {
        super.update(parentRect, render, input, rect);
        text.draw(render, rect.getPos());
        if (shouldAutoSize()) {
            Size textSize = text.getSze(render);
            applyAutoSize(parentRect, textSize.width(), textSize.height());
        }
    }

    @Override
    public void applyDefaultStyle(final Theme theme) {
        if (text.getStyle() == null) {
            text.setStyle(theme.getTextStyle());
        }
    }
}
