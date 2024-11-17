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

package com.github.yuri6037.sje2d.asset.factory.animation;

import com.github.yuri6037.sje2d.asset.Animation;
import com.github.yuri6037.sje2d.asset.engine.AssetURL;
import com.github.yuri6037.sje2d.asset.engine.map.AssetDepMap;
import com.github.yuri6037.sje2d.asset.factory.base.BaseLoader;
import com.github.yuri6037.sje2d.math.MathUtils;
import com.github.yuri6037.sje2d.util.ImageUtils;

//CHECKSTYLE OFF: AvoidStarImport
import static org.lwjgl.opengl.GL12.*;
//CHECKSTYLE ON

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

public abstract class AnimationLoader extends BaseLoader<Animation> {
    private static final int MAX_TEXTURE_SIZE = 8192;

    private ByteBuffer buffer;
    private int numColumns;
    private int numRows;

    //CHECKSTYLE OFF: VisibilityModifier

    /**
     * The list of frames.
     */
    protected final List<BufferedImage> frames = new ArrayList<>();

    /**
     * The target animation FPS.
     */
    protected int fps = 30;

    /**
     * The width of a single frame of animation.
     */
    protected int frameWidth = 0;

    /**
     * The height of a single frame of animation.
     */
    protected int frameHeight = 0;

    //CHECKSTYLE ON

    /**
     * A base animation loader.
     * @param url the target URL to load.
     */
    public AnimationLoader(final AssetURL url) {
        super(url);
    }

    /**
     * Called to build the list of frames and the properties of this animation.
     * @throws Exception if the animation could not be built.
     */
    protected abstract void build() throws Exception;

    private BufferedImage genBitmap() {
        if (frames.isEmpty() || frameWidth == 0 || frameHeight == 0) {
            return null;
        }
        if (!MathUtils.isPowerOfTwo(frameWidth) || !MathUtils.isPowerOfTwo(frameHeight)) {
            throw new IllegalArgumentException("Animation frame size is not a power of 2");
        }
        numColumns = (int) Math.ceil((double) (frameHeight * frames.size()) / (double) MAX_TEXTURE_SIZE);
        numRows = Math.min(frames.size(), MAX_TEXTURE_SIZE / frameHeight);
        BufferedImage output = new BufferedImage(numColumns * frameWidth, numRows * frameHeight,
                BufferedImage.TYPE_INT_ARGB);
        Graphics2D graphics = output.createGraphics();
        int x = 0;
        int y = 0;
        for (BufferedImage frame: frames) {
            graphics.drawImage(frame, x, y, null);
            y += frameHeight;
            if (y >= MAX_TEXTURE_SIZE) {
                y = 0;
                x += frameWidth;
            }
        }
        return output;
    }

    @Override
    public final Result load(final AssetDepMap dependencies) throws Exception {
        build();
        BufferedImage bitmap = genBitmap();
        if (bitmap == null) {
            return Result.none();
        }
        buffer = ImageUtils.imageToBuffer(bitmap);
        return Result.ready();
    }

    @Override
    protected final Animation createAsset() throws Exception {
        Animation animation = new Animation(buffer, frameWidth, frameHeight, fps, numRows, numColumns, frames.size());
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_CLAMP_TO_EDGE);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_CLAMP_TO_EDGE);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST);
        return animation;
    }
}
