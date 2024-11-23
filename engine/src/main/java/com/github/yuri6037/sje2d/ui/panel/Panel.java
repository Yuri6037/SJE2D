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
 * A Panel is a panel that allows absolute or proportional positioning of its contents.
 */
@Reflect
public final class Panel extends BasePanel {
    private boolean centered = false;
    private final Rect componentRect = new Rect();

    /**
     * Creates a new instance of a Panel.
     */
    public Panel() {
        addParam("centered", Boolean.class, this::setCentered);
    }

    /**
     * Sets whether this panel is centered.
     * @param centered1 true if this panel should be centered, false otherwise.
     * @return this for chaining operations.
     */
    public Panel setCentered(final boolean centered1) {
        centered = centered1;
        return this;
    }

    @Override
    public void update(final Rect parentRect, final IRender render, final IInput input, final Rect rect) {
        super.update(parentRect, render, input, rect);
        float maxWidth = 0;
        float maxHeight = 0;
        for (Component c: getComponents()) {
            Size cRenderSize = isProportional()
                    ? new Size(c.getSize().width() * rect.getSize().width(), c.getSize().height()
                    * rect.getSize().height())
                    : c.getSize();
            Point cRenderPos = isProportional()
                    ? new Point(rect.getPos().x() + (c.getPos().x() * rect.getSize().width()
                    - (centered ? cRenderSize.width() / 2f : 0f)),
                    rect.getPos().y() + (c.getPos().y() * rect.getSize().height()
                            - (centered ? cRenderSize.height() / 2f : 0f)))
                    : new Point(rect.getPos().x() + (c.getPos().x() - (centered ? cRenderSize.width() / 2f : 0f)),
                    rect.getPos().y() + (c.getPos().y() - (centered ? cRenderSize.height() / 2f : 0f)));
            componentRect.setSize(cRenderSize);
            componentRect.setPos(cRenderPos);
            c.update(isProportional() ? rect : null, render, input, componentRect);
            if (cRenderPos.x() + cRenderSize.width() > maxWidth) {
                maxWidth = cRenderPos.x() + cRenderSize.width();
            }
            if (cRenderPos.y() + cRenderSize.height() > maxHeight) {
                maxHeight = cRenderPos.y() + cRenderSize.height();
            }
        }
        applyAutoSize(parentRect, maxWidth - rect.getPos().x(), maxHeight - rect.getPos().y());
    }
}
