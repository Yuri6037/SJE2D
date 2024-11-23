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

import com.github.yuri6037.sje2d.reflect.Reflect;
import com.github.yuri6037.sje2d.render.Point;
import com.github.yuri6037.sje2d.render.Size;
import com.github.yuri6037.sje2d.ui.component.Component;
import com.github.yuri6037.sje2d.ui.core.input.IInput;
import com.github.yuri6037.sje2d.ui.core.render.IRender;
import com.github.yuri6037.sje2d.ui.core.render.Rect;

/**
 * A VPanel is a panel that aligns its contents vertically.
 */
@Reflect
public final class VPanel extends BasePanel {
    private float spacing = 0.0f;
    private final Rect componentRect = new Rect();

    /**
     * Creates a new instance of a VPanel.
     */
    public VPanel() {
        addParam("spacing", Float.class, this::setSpacing);
    }

    /**
     * Sets the spacing between items in this panel (this defaults to 0).
     * @param spacing1 the new spacing between items.
     * @return this for chaining operations.
     */
    public VPanel setSpacing(final float spacing1) {
        this.spacing = spacing1;
        return this;
    }

    @Override
    public void update(final Rect parentRect, final IRender render, final IInput input, final Rect rect) {
        super.update(parentRect, render, input, rect);
        float x = rect.getPos().x();
        float y = rect.getPos().y();
        float maxWidth = 0;
        for (Component c: getComponents()) {
            Size cRenderSize = isProportional()
                    ? new Size(c.getSize().width() * rect.getSize().width(), c.getSize().height()
                    * rect.getSize().height())
                    : c.getSize();
            componentRect.setSize(cRenderSize);
            componentRect.setPos(new Point(x, y));
            c.update(isProportional() ? rect : null, render, input, componentRect);
            if (cRenderSize.width() > maxWidth) {
                maxWidth = cRenderSize.width();
            }
            if (isProportional()) {
                y += c.getSize().height() * rect.getSize().height();
            } else {
                y += c.getSize().height();
            }
            y += spacing;
        }
        applyAutoSize(parentRect, maxWidth, y - rect.getPos().y());
    }
}
