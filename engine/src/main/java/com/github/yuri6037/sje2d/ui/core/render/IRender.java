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
import com.github.yuri6037.sje2d.render.Color;
import com.github.yuri6037.sje2d.render.Size;
import com.github.yuri6037.sje2d.util.UTF32Str;

/**
 * The UI Render Engine entry point.
 */
public interface IRender {
    /**
     * Sets the font for all future text draw calls.
     * @param font the new font to use.
     */
    void setFont(Font font);

    /**
     * Sets the color for all future drawing operations.
     * @param color the new color to use.
     */
    void setColor(Color color);

    /**
     * Sets the texture for all future drawing operations, excluding text rendering which uses its own font bitmaps.
     * @param texture the new texture to use.
     */
    void setTexture(Texture texture);

    /**
     * Sets the shadow color for all future 3D text draw calls.
     * @param color the new shadow color to use.
     */
    void setShadowColor(Color color);

    /**
     * Sets the offset for all future 3D text draw calls.
     * @param offset3d the new offset to use.
     */
    void set3DOffset(float offset3d);

    /**
     * Draws a filled rectangle with no outline.
     * @param x the X coordinate of the rectangle.
     * @param y the Y coordinate of the rectangle.
     * @param width the rectangle width.
     * @param height the rectangle height.
     */
    void drawRect(float x, float y, float width, float height);

    /**
     * Draws a filled rectangle with rounded corners.
     * @param x the X coordinate of the rectangle.
     * @param y the Y coordinate of the rectangle.
     * @param width the rectangle width.
     * @param height the rectangle height.
     * @param borderRadius the border radius.
     */
    void drawRoundedRect(float x, float y, float width, float height, float borderRadius);

    /**
     * Draws a rectangle outline without a filled rectangle.
     * @param x the X coordinate of the rectangle.
     * @param y the Y coordinate of the rectangle.
     * @param width the rectangle width.
     * @param height the rectangle height.
     * @param lineWidth the stroke width.
     */
    void drawOutlineRect(float x, float y, float width, float height, float lineWidth);

    /**
     * Draws a 3D UTF-32 text.
     * @param text the text to draw.
     * @param x the X coordinate of the text.
     * @param y the Y coordinate of the text.
     */
    void draw3DText(UTF32Str text, float x, float y);

    /**
     * Draws a UTF-32 text.
     * @param text the text to draw.
     * @param x the X coordinate of the text.
     * @param y the Y coordinate of the text.
     */
    void drawText(UTF32Str text, float x, float y);

    /**
     * Computes the size of a UTF-32 text.
     * @param text the text to compute the size of.
     * @return the size of the input text.
     */
    Size getTextSize(UTF32Str text);

    /**
     * Enables the scissor rectangle.
     * @param x the X coordinate of the rectangle.
     * @param y the Y coordinate of the rectangle.
     * @param width the rectangle width.
     * @param height the rectangle height.
     */
    void enableScissorRect(float x, float y, float width, float height);

    /**
     * Disables the scissor rectangle.
     */
    void disableScissorRect();
}
