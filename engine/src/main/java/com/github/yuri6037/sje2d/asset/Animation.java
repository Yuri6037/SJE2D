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

package com.github.yuri6037.sje2d.asset;

import com.github.yuri6037.sje2d.render.Rect;
import com.github.yuri6037.sje2d.util.Timer;

import java.nio.ByteBuffer;

public class Animation extends Texture {
    private final int frameWidth;
    private final int frameHeight;
    private final int frameCount;
    private final int numRows;
    private final int height;
    private final int width;
    private final double fps;

    /**
     * Creates a new texture from a buffer and its size.
     * @param buffer the buffer containing all texel data.
     * @param frameWidth the width of a single animation frame.
     * @param frameHeight the height of a single animation frame.
     * @param fps the number of FPS of the new animation.
     * @param numRows the number of rows in the animation bitmap.
     * @param numColumns the number of columns in the animation bitmap.
     * @param frameCount the number of frames in the animation.
     */
    public Animation(final ByteBuffer buffer, final int frameWidth, final int frameHeight, final int fps,
                     final int numRows, final int numColumns, final int frameCount) {
        super(buffer, frameWidth * numColumns, frameHeight * numRows);
        this.fps = (double) fps;
        this.frameHeight = frameHeight;
        this.frameWidth = frameWidth;
        this.numRows = numRows;
        this.frameCount = frameCount;
        this.height = frameHeight * numRows;
        this.width = frameWidth * numColumns;
    }

    /**
     * @return the height of a single animation frame.
     */
    public int getFrameHeight() {
        return frameHeight;
    }

    /**
     * @return the width of a single animation frame.
     */
    public int getFrameWidth() {
        return frameWidth;
    }

    /**
     * Gets the frame rectangle for the current frame.
     * @param timer the application timer.
     * @return the rectangle to apply to render only the corresponding animation frame.
     */
    public Rect getFrameRect(final Timer timer) {
        double correctedFrame = timer.getTime() * fps;
        int frame = (int) correctedFrame % frameCount;
        int gridx = frame / numRows;
        int gridy = frame % numRows;
        float x = ((float) gridx * (float) frameWidth) / (float) width;
        float y = ((float) gridy * (float) frameHeight) / (float) height;
        float w = (float) frameWidth / (float) width;
        float h = (float) frameHeight / (float) height;
        return Rect.fromXYWH(x, y, w, h);
    }
}
