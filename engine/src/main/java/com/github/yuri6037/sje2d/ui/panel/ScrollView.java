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

package com.github.yuri6037.sje2d.ui.panel;

import com.github.yuri6037.sje2d.input.Key;
import com.github.yuri6037.sje2d.math.MathUtils;
import com.github.yuri6037.sje2d.reflect.Reflect;
import com.github.yuri6037.sje2d.render.Point;
import com.github.yuri6037.sje2d.ui.asset.Theme;
import com.github.yuri6037.sje2d.ui.asset.style.RectangleStyle;
import com.github.yuri6037.sje2d.ui.asset.style.ScrollbarStyle;
import com.github.yuri6037.sje2d.ui.asset.style.CompositeStyle;
import com.github.yuri6037.sje2d.ui.component.Component;
import com.github.yuri6037.sje2d.ui.core.input.DummyInput;
import com.github.yuri6037.sje2d.ui.core.input.IInput;
import com.github.yuri6037.sje2d.ui.core.render.IRender;
import com.github.yuri6037.sje2d.ui.core.render.Rect;
import com.github.yuri6037.sje2d.ui.core.render.primitive.Rectangle;

import java.util.Map;

@Reflect
public final class ScrollView extends Component implements IPanel {
    /**
     * The theme key for a scrollbar.
     */
    public static final Theme.Key THEME_KEY = new Theme.Key("SCROLLBAR", CompositeStyle.class);

    private Component inner;
    private final Rect viewRect = new Rect();
    private float innerLocX = 0;
    private float innerLocY = 0;
    private float offsetX = 0;
    private float offsetY = 0;
    private final Rectangle scrollbar = new Rectangle();
    private float minScrollbarSize;
    private float scrollbarSize;

    /**
     * Creates a new instance of a ScrollView.
     */
    public ScrollView() {
        addParam("scrollbarStyle", CompositeStyle.class, this::setScrollbarStyle);
    }

    /**
     * Sets the style of the scrollbar.
     * @param style the new style.
     * @return this for chaining operations.
     */
    public ScrollView setScrollbarStyle(final CompositeStyle style) {
        ScrollbarStyle sc = style.get(ScrollbarStyle.class, "Main");
        minScrollbarSize = sc.getMinSize();
        scrollbarSize = sc.getSize();
        scrollbar.setStyle(style.get(RectangleStyle.class, "Base"));
        return this;
    }

    @Override
    public ScrollView add(final Component component) {
        inner = component;
        return this;
    }

    @Override
    public ScrollView remove(final Component component) {
        inner = null;
        return this;
    }

    @Override
    public void update(final Rect parentRect, final IRender render, final IInput input, final Rect rect) {
        super.update(parentRect, render, input, rect);
        viewRect.setPos(new Point(rect.getPos().x() + innerLocX, rect.getPos().y() + innerLocY));
        viewRect.setSize(this.inner.getSize());

        float scrollXH = MathUtils.clamp(rect.getSize().width() / (this.inner.getSize().width()
                / rect.getSize().width()), minScrollbarSize, rect.getSize().width());
        float scrollYH = MathUtils.clamp(rect.getSize().height() / (this.inner.getSize().height()
                / rect.getSize().height()), minScrollbarSize, rect.getSize().height());
        boolean isYScrollable = scrollYH < rect.getSize().height();
        boolean isXScrollable = scrollXH < rect.getSize().width();
        boolean isMouseIn = Component.isMouseIn(input, rect);

        if (isMouseIn) {
            if (isXScrollable) {
                innerLocX += input.getMouseWheel().getX();
                innerLocX = -MathUtils.clamp(-innerLocX, 0f, inner.getSize().width()
                        - rect.getSize().width());
            }
            if (isYScrollable) {
                innerLocY += input.getMouseWheel().getY();
                innerLocY = -MathUtils.clamp(-innerLocY, 0f, inner.getSize().height()
                        - rect.getSize().height());
            }
        }

        if (isMouseIn && input.isKeyDown(Key.MOUSE_MIDDLE)) {
            if (offsetX == 0 || offsetY == 0) {
                offsetX = innerLocX - input.getMouse().getX();
                offsetY = innerLocY - input.getMouse().getY();
            }

            if (isXScrollable) {
                innerLocX = input.getMouse().getX() + offsetX;
                innerLocX = -MathUtils.clamp(-innerLocX, 0f, inner.getSize().width()
                        - rect.getSize().width());
            }
            if (isYScrollable) {
                innerLocY = input.getMouse().getY() + offsetY;
                innerLocY = -MathUtils.clamp(-innerLocY, 0f, inner.getSize().height()
                        - rect.getSize().height());
            }
        } else {
            offsetX = 0;
            offsetY = 0;
        }

        render.enableScissorRect(rect.getPos().x(), rect.getPos().y(), rect.getSize().width(), rect.getSize().height());
        inner.update(null, render, isMouseIn ? input : DummyInput.INSTANCE, viewRect);
        render.disableScissorRect();

        if (isMouseIn) {
            float scrollYP = -(innerLocY / (inner.getSize().height() - rect.getSize().height()));
            float scrollYPP = scrollYP * (rect.getSize().height() - scrollYH);

            float scrollXP = -(innerLocX / (inner.getSize().width() - rect.getSize().width()));
            float scrollXPP = scrollXP * (rect.getSize().width() - scrollXH);

            if (isYScrollable) {
                scrollbar.draw(render, rect.getPos().x() + rect.getSize().width() - scrollbarSize,
                        rect.getPos().y() + scrollYPP, scrollbarSize, scrollYH);
            }
            if (isXScrollable) {
                scrollbar.draw(render, rect.getPos().x() + scrollXPP, rect.getPos().y() + rect.getSize().height()
                        - scrollbarSize, scrollXH, scrollbarSize);
            }
        }
    }

    @Override
    public void applyDefaultStyle(final Theme theme) {
        if (scrollbar.getStyle() == null) {
            setScrollbarStyle(theme.getStyle(CompositeStyle.class, THEME_KEY));
        }
    }

    @Override
    public Component instantiate(final Map<String, Component> componentsById) throws CloneNotSupportedException {
        ScrollView clone = (ScrollView) super.instantiate(componentsById);
        clone.inner = inner.instantiate(componentsById);
        return clone;
    }
}
