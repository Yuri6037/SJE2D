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

import com.github.yuri6037.sje2d.render.Color;
import com.github.yuri6037.sje2d.ui.core.render.IRender;

/**
 * A 3D text primitive with a shadow.
 */
public final class Text3D extends Text {
    private float offset = 5;
    private Color shadowColor = Color.WHITE;

    /**
     * Sets the shadow color of this primitive.
     * @param shadowColor the new shadow color.
     */
    public void setShadowColor(final Color shadowColor) {
        this.shadowColor = shadowColor;
    }

    /**
     * Sets the 3D offset of this primitive.
     * @param offset the new 3D offset.
     */
    public void setOffset(final float offset) {
        this.offset = offset;
    }

    /**
     * @return the 3D offset of the text to be rendered.
     */
    public float getOffset() {
        return offset;
    }

    /**
     * Draws this primitive matching the requested style.
     * @param render an instance of the UI rendering engine.
     * @param x the X coordinate of the text.
     * @param y the Y coordinate of the text.
     */
    @Override
    public void draw(final IRender render, final float x, final float y) {
        if (getText() == null) {
            return;
        }
        render.setFont(getFont() != null ? getFont() : getStyle().getFont());
        render.setColor(getColor() != null ? getColor() : getStyle().getColor());
        render.set3DOffset(offset);
        render.setShadowColor(shadowColor);
        render.draw3DText(getText(), x, y);
    }
}
