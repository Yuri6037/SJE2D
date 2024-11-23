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

package com.github.yuri6037.sje2d.ui.core.render;

import com.github.yuri6037.sje2d.asset.Font;
import com.github.yuri6037.sje2d.asset.Texture;
import com.github.yuri6037.sje2d.asset.engine.manager.AssetManagerProxy;
import com.github.yuri6037.sje2d.util.UTF32Str;
import com.github.yuri6037.sje2d.window.Window;
import com.github.yuri6037.sje2d.render.FontRender;
import com.github.yuri6037.sje2d.render.Render;
import com.github.yuri6037.sje2d.render.Color;
import com.github.yuri6037.sje2d.render.Size;

import java.util.HashMap;

//CHECKSTYLE OFF: AvoidStarImport
import static org.lwjgl.opengl.GL12.*;
//CHECKSTYLE ON

public final class SimpleRender implements IRender {
    private final HashMap<Font, FontRender> fonts = new HashMap<>();
    private final Render render;
    private final AssetManagerProxy assets;
    private final Window window;

    private FontRender curFont = null;

    /**
     * Creates a new instance of a simple UI Render Engine.
     * @param assets an instance of the assets manager.
     * @param window an instance of the application window.
     * @param render an instance of the SJE2D base Render engine.
     */
    public SimpleRender(final AssetManagerProxy assets, final Window window, final Render render) {
        this.assets = assets;
        this.render = render;
        this.window = window;
    }

    @Override
    public void setFont(final Font font) {
        FontRender render1 = fonts.computeIfAbsent(font, FontRender::new);
        if (render1 != curFont) {
            curFont = render1;
        }
    }

    @Override
    public void setColor(final Color color) {
        render.setColor(color);
        if (curFont != null) {
            curFont.setTextColor(color);
        }
    }

    @Override
    public void setTexture(final Texture texture) {
        if (texture == null) {
            render.setTexture(Texture.NULL);
        } else {
            render.setTexture(texture);
        }
    }

    @Override
    public void setShadowColor(final Color color) {
        if (curFont == null) {
            return;
        }
        curFont.setShadowColor(color);
    }

    @Override
    public void set3DOffset(final float offset3d) {
        if (curFont == null) {
            return;
        }
        curFont.set3DOffset(offset3d);
    }

    @Override
    public void drawRect(final float x, final float y, final float width, final float height) {
        render.drawRect(x, y, width, height);
    }

    @Override
    public void drawOutlineRect(final float x, final float y, final float width, final float height,
                                final float lineWidth) {
        render.drawOutlineRect(x, y, width, height, lineWidth);
    }

    @Override
    public void draw3DText(final UTF32Str text, final float x, final float y) {
        if (curFont == null) {
            return;
        }
        curFont.draw3DString(assets, text, x, y);
    }

    @Override
    public void drawText(final UTF32Str text, final float x, final float y) {
        if (curFont == null) {
            return;
        }
        curFont.drawString(assets, text, x, y);
    }

    @Override
    public Size getTextSize(final UTF32Str text) {
        if (curFont == null) {
            return Size.ZERO;
        }
        return curFont.getStringSize(assets, text);
    }

    @Override
    public void enableScissorRect(final float x, final float y, final float width, final float height) {
        glEnable(GL_SCISSOR_TEST);
        glScissor((int) (x * window.getScaleX()), (int) ((window.getHeight() - y - height) * window.getScaleY()),
                (int) (width * window.getScaleX()), (int) (height * window.getScaleY()));
    }

    @Override
    public void disableScissorRect() {
        glDisable(GL_SCISSOR_TEST);
    }

    @Override
    public void drawRoundedRect(final float x, final float y, final float width, final float height,
                                final float borderRadius) {
        drawRect(x, y, width, height);
        render.drawCircle(x, y, borderRadius, -(float) (Math.PI), -(float) (Math.PI / 2), 6);
        render.drawCircle(x, y + height, borderRadius, (float) (Math.PI), (float) (Math.PI / 2), 6);
        render.drawCircle(x + width, y, borderRadius, -(float) (Math.PI / 2), 0, 6);
        render.drawCircle(x + width, y + height, borderRadius, 0, (float) (Math.PI / 2), 6);
        drawRect(x - borderRadius, y, borderRadius, height);
        drawRect(x + width, y, borderRadius, height);
        drawRect(x, y -  borderRadius, width, borderRadius);
        drawRect(x, y + height, width, borderRadius);
    }
}
