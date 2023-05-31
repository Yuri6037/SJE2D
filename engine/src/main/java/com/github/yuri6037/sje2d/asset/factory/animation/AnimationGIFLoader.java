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

import com.github.yuri6037.sje2d.asset.engine.AssetURL;
import com.github.yuri6037.sje2d.asset.engine.system.stream.IAssetStream;
import com.github.yuri6037.sje2d.asset.engine.system.stream.StreamUtils;

import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.InputStream;

public final class AnimationGIFLoader extends AnimationLoader {
    private final InputStream stream;

    /**
     * Creates a new animation GIF loader.
     * @param url the asset URL.
     * @param stream the asset stream.
     */
    public AnimationGIFLoader(final AssetURL url, final IAssetStream stream) {
        super(url);
        this.stream = StreamUtils.makeInputStream(stream);
    }

    @Override
    protected void build() throws Exception {
        ImageReader reader = ImageIO.getImageReadersBySuffix("gif").next();
        BufferedImage frame = null;
        reader.setInput(ImageIO.createImageInputStream(stream));
        int i = reader.getMinIndex();
        int max = reader.getNumImages(true);
        while (i < max) {
            BufferedImage brokenFrame = reader.read(i);
            if (frameWidth < brokenFrame.getWidth()) {
                frameWidth = brokenFrame.getWidth();
            }
            if (frameHeight < brokenFrame.getHeight()) {
                frameHeight = brokenFrame.getHeight();
            }
            BufferedImage frame1 = new BufferedImage(brokenFrame.getWidth(), brokenFrame.getHeight(),
                    BufferedImage.TYPE_INT_ARGB);
            Graphics2D graphics = frame1.createGraphics();
            if (frame != null) {
                int reAlignedX = brokenFrame.getWidth() / 2 - frame.getWidth() / 2;
                int reAlignedY = brokenFrame.getHeight() / 2 - frame.getHeight() / 2;
                graphics.drawImage(frame, reAlignedX, reAlignedY, null);
            }
            graphics.drawImage(brokenFrame, 0, 0, null);
            frames.add(frame1);
            frame = frame1;
            ++i;
        }
        fps = Integer.parseInt(url.getParameter("fps", "30"));
    }
}
