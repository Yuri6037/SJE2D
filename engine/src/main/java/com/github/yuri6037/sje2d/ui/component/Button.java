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

import com.github.yuri6037.sje2d.input.Key;
import com.github.yuri6037.sje2d.reflect.Reflect;
import com.github.yuri6037.sje2d.render.Size;
import com.github.yuri6037.sje2d.ui.asset.Theme;
import com.github.yuri6037.sje2d.ui.asset.style.RectangleStyle;
import com.github.yuri6037.sje2d.ui.asset.style.TextStyle;
import com.github.yuri6037.sje2d.ui.asset.style.CompositeStyle;
import com.github.yuri6037.sje2d.ui.core.input.IInput;
import com.github.yuri6037.sje2d.ui.core.render.IRender;
import com.github.yuri6037.sje2d.ui.core.render.Rect;
import com.github.yuri6037.sje2d.ui.core.render.primitive.Rectangle;
import com.github.yuri6037.sje2d.ui.core.render.primitive.Text;
import com.github.yuri6037.sje2d.util.UTF32Str;

@Reflect
public final class Button extends Component {
    /**
     * The theme key for a button.
     */
    public static final Theme.Key THEME_KEY = new Theme.Key("BUTTON", CompositeStyle.class);

    private Runnable action = null;
    private final Rectangle normal = new Rectangle();
    private final Rectangle hover = new Rectangle();
    private final Rectangle pressed = new Rectangle();
    private final Text text = new Text();
    private UTF32Str caption;

    /**
     * Creates a new Button component.
     */
    public Button() {
        addParam("style", CompositeStyle.class, this::setStyle);
        addParam("text", UTF32Str.class, this::setText);
    }

    /**
     * Sets the style of this button.
     * @param style the new style of the button.
     * @return this for chaining operations.
     */
    public Button setStyle(final CompositeStyle style) {
        normal.setStyle(style.get(RectangleStyle.class, "Base"));
        hover.setStyle(style.get(RectangleStyle.class, "Hover"));
        pressed.setStyle(style.get(RectangleStyle.class, "Pressed"));
        text.setStyle(style.get(TextStyle.class, "Text"));
        return this;
    }

    /**
     * Sets the action to perform when this button is clicked.
     * @param action1 the new action to execute.
     * @return this for chaining operations.
     */
    public Button setAction(final Runnable action1) {
        action = action1;
        return this;
    }

    /**
     * Sets the text to be rendered as the caption of the button.
     * @param text1 the new text already in UTF32 format.
     * @return this for chaining operations.
     */
    public Button setText(final UTF32Str text1) {
        caption = Text.convert(text1);
        return this;
    }

    /**
     * Sets the text to be rendered as the caption of the button.
     * @param text1 the new text as a standard java String to be converted to UTF32.
     * @return this for chaining operations.
     */
    public Button setText(final String text1) {
        caption = Text.convert(text1);
        return this;
    }

    @Override
    public void update(final Rect parentRect, final IRender render, final IInput input, final Rect rect) {
        super.update(parentRect, render, input, rect);
        if (Component.isMouseIn(input, rect)) {
            if (action != null && input.wasKeyDown(Key.MOUSE_LEFT)) {
                action.run();
            }
            if (input.isKeyDown(Key.MOUSE_LEFT)) {
                pressed.draw(render, rect.getPos(), rect.getSize());
            } else {
                hover.draw(render, rect.getPos(), rect.getSize());
            }
        } else {
            normal.draw(render, rect.getPos(), rect.getSize());
        }
        Size textSize = text.getSze(render, caption);
        float posx = rect.getPos().x() + rect.getSize().width() / 2f - textSize.width() / 2f;
        float posy = rect.getPos().y() + rect.getSize().height() / 2f - textSize.height() / 2f;
        text.draw(render, posx, posy, caption);
        applyAutoSize(parentRect, textSize.width(), textSize.height());
    }

    @Override
    public void applyDefaultStyle(final Theme theme) {
        if (normal.getStyle() == null) {
            setStyle(theme.getStyle(CompositeStyle.class, THEME_KEY));
        }
    }
}
