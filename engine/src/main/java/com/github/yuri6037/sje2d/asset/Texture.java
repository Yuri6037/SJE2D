/*
 * Copyright (c) 2023, SJE2D
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

package com.github.yuri6037.sje2d.asset;

import com.github.yuri6037.sje2d.asset.engine.system.IAsset;

//CHECKSTYLE OFF: AvoidStarImport
import java.nio.ByteBuffer;

import static org.lwjgl.opengl.GL11.*;
//CHECKSTYLE ON

public class Texture implements IAsset {
    /**
     * Hack to allow calling setTexture(null) without getting bullshit ambiguous errors from Java.
     */
    public static final Texture NULL = null;

    private final int id;

    /**
     * Creates a new texture from a buffer and its size.
     * @param buffer the buffer containing all texel data.
     * @param width the texture width.
     * @param height the texture height.
     */
    public Texture(final ByteBuffer buffer, final int width, final int height) {
        glEnable(GL_TEXTURE_2D);
        int texture = glGenTextures();
        glBindTexture(GL_TEXTURE_2D, texture);
        glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA, width, height, 0, GL_RGBA, GL_UNSIGNED_BYTE, buffer);
        id = texture;
    }

    /**
     * @return The GL index of the 2D texture object.
     */
    public final int getGLId() {
        return id;
    }

    /**
     * Unloads this texture.
     * NOTE: when overriding this function, you should call back this implementation as otherwise the underlying
     * GL texture object may leak.
     */
    @Override
    public void unload() {
        glDeleteTextures(id);
    }
}
