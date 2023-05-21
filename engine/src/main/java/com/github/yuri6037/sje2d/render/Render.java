/* 
 *  Copyright (c) 2023, SJE2D
 *  
 *  All rights reserved.
 *  
 *  Redistribution and use in source and binary forms, with or without modification,
 *  are permitted provided that the following conditions are met:
 *  
 *      * Redistributions of source code must retain the above copyright notice,
 *        this list of conditions and the following disclaimer.
 *      * Redistributions in binary form must reproduce the above copyright notice,
 *        this list of conditions and the following disclaimer in the documentation
 *        and/or other materials provided with the distribution.
 *      * Neither the name of BlockProject 3D nor the names of its contributors
 *        may be used to endorse or promote products derived from this software
 *        without specific prior written permission.
 *  
 *  THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
 *  "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT
 *  LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR
 *  A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR
 *  CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL,
 *  EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO,
 *  PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR
 *  PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF
 *  LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
 *  NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 *  SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

package com.github.yuri6037.sje2d.render;

import com.github.yuri6037.sje2d.asset.Texture;
import com.github.yuri6037.sje2d.asset.engine.map.AssetStore;

import java.util.Objects;

//CHECKSTYLE OFF: AvoidStarImport
import static org.lwjgl.opengl.GL11.*;
//CHECKSTYLE ON

public final class Render {
    private Rect textureRect = new Rect(0, 0, 1, 1);
    private Point transformCenter = new Point(0, 0);
    private float rotation = 0;
    private float scale = 1;

    /**
     * Sets the transform center point. This gets multiplied to the size of the element in the drawing function.
     * @param p the center point in normalized coordinates [0;1].
     */
    public void setTransformCenter(final Point p) {
        transformCenter = Objects.requireNonNullElseGet(p, () -> new Point(0, 0));
    }

    /**
     * Sets a uniform scale transformation to apply to all future rendering operations.
     * @param scale the new scale to apply.
     */
    public void setScale(final float scale) {
        this.scale = scale;
    }

    /**
     * Sets a rotation transformation to apply to all future rendering operations.
     * @param angle the angle in degrees.
     */
    public void setRotation(final float angle) {
        rotation = angle;
    }

    /**
     * Sets the normalized texture coordinates as a rectangle. The default is (0, 0, 1, 1).
     * @param rect the new rectangle.
     */
    public void setTextureRect(final Rect rect) {
        textureRect = Objects.requireNonNullElseGet(rect, () -> new Rect(0, 0, 1, 1));
    }

    /**
     * Sets a color to apply to all future rendering operations.
     * @param color the color object instance.
     */
    public void setColor(final Color color) {
        glColor4f(color.r(), color.g(), color.b(), color.a());
    }

    /**
     * Sets a texture to apply to all future rendering operations.
     * @param texture the texture object instance.
     */
    public void setTexture(final Texture texture) {
        if (texture == null) {
            glDisable(GL_TEXTURE_2D);
            return;
        }
        glEnable(GL_TEXTURE_2D);
        glBindTexture(GL_TEXTURE_2D, texture.getGLId());
    }

    /**
     * Sets a texture to apply to all future rendering operations.
     * @param asset the asset reference.
     */
    public void setTexture(final AssetStore<Texture>.Ref asset) {
        setTexture(asset.get());
    }

    /**
     * Draws a rectangle with current transformation options.
     * @param x x coordinate of the rectangle.
     * @param y y coordinate of the rectangle.
     * @param width width of the rectangle.
     * @param height height of the rectangle.
     */
    public void drawRect(final float x, final float y, final float width, final float height) {
        if (rotation != 0 || scale != 1) {
            glPushMatrix();
            glTranslatef(x + width * transformCenter.x(), y + height * transformCenter.y(), 0);
            if (rotation != 0) {
                glRotatef(rotation, 0, 0, 1);
            }
            if (scale != 1) {
                glScalef(scale, scale, 1);
            }
            glTranslatef(-(x + width * transformCenter.x()), -(y + height * transformCenter.y()), 0);
        }
        glBegin(GL_QUADS);
        {
            glTexCoord2f(textureRect.x(), textureRect.y());
            glVertex2f(x, y);

            glTexCoord2f(textureRect.x1(), textureRect.y());
            glVertex2f(width + x, y);

            glTexCoord2f(textureRect.x1(), textureRect.y1());
            glVertex2f(width + x, height + y);

            glTexCoord2f(textureRect.x(), textureRect.y1());
            glVertex2f(x, height + y);
        }
        glEnd();
        if (rotation != 0 || scale != 1) {
            glPopMatrix();
        }
    }
}
