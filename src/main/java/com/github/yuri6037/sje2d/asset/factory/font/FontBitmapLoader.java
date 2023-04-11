// Copyright (c) 2023, SJE2D
//
// All rights reserved.
//
// Redistribution and use in source and binary forms, with or without modification,
// are permitted provided that the following conditions are met:
//
//     * Redistributions of source code must retain the above copyright notice,
//       this list of conditions and the following disclaimer.
//     * Redistributions in binary form must reproduce the above copyright notice,
//       this list of conditions and the following disclaimer in the documentation
//       and/or other materials provided with the distribution.
//     * Neither the name of BlockProject 3D nor the names of its contributors
//       may be used to endorse or promote products derived from this software
//       without specific prior written permission.
//
// THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
// "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT
// LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR
// A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR
// CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL,
// EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO,
// PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR
// PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF
// LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
// NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
// SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.

package com.github.yuri6037.sje2d.asset.factory.font;

import com.github.yuri6037.sje2d.asset.FontBitmap;
import com.github.yuri6037.sje2d.asset.engine.AssetURL;
import com.github.yuri6037.sje2d.asset.engine.map.AssetDepMap;
import com.github.yuri6037.sje2d.asset.factory.BaseLoader;
import com.github.yuri6037.sje2d.util.ImageUtils;
import com.github.yuri6037.sje2d.util.MathUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Color;
import java.awt.image.BufferedImage;
import java.nio.ByteBuffer;
import java.util.HashMap;

public abstract class FontBitmapLoader extends BaseLoader<FontBitmap> {
    private static final Logger LOGGER = LoggerFactory.getLogger(FontBitmapLoader.class);

    /**
     * Creates a new FontBitmapLoader.
     * @param url the asset url that is going to be loaded.
     */
    public FontBitmapLoader(final AssetURL url) {
        super(url);
    }

    /**
     * Builds the font object used to render the bitmap.
     * @return a new Font object.
     * @throws Exception if the font failed to build.
     */
    protected abstract Font buildFont() throws Exception;

    private int width;
    private int charHeight;
    private final HashMap<Character, Integer> charWidth = new HashMap<>();
    private ByteBuffer buffer;

    @Override
    public final Result load(final AssetDepMap dependencies) throws Exception {
        width = Integer.parseInt(url.getParameter("width", "512"));
        if (!MathUtils.isPowerOfTwo(width)) {
            throw new IllegalArgumentException("Bitmap size is not a power of 2");
        }
        int blockSize =  width / 16;
        int plane = Integer.parseInt(url.getParameter("plane", "0"));
        Font font = buildFont();
        LOGGER.debug("Building font bitmap ({}x{} - {}) for plane #{}, with font '{}'...", width, width, blockSize,
                plane, font);
        if (plane * 256 > 1114112) {
            throw new IllegalArgumentException("Character plane is out of range");
        }
        @SuppressWarnings("SuspiciousNameCombination")
        BufferedImage image = new BufferedImage(width, width, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = image.createGraphics();
        g2d.setFont(font);
        g2d.setColor(Color.WHITE);
        int ch = g2d.getFontMetrics().getHeight();
        char c = (char) (plane * 256);
        for (int i = 0; i != 16; ++i) {
            for (int j = 0; j != 16; ++j) {
                String cs = String.valueOf(c);
                int cw = g2d.getFontMetrics().stringWidth(cs);
                charWidth.put(c, cw);
                int posx = j * blockSize + blockSize / 2 - cw / 2;
                int posy = i * blockSize + blockSize / 2 - ch / 2;
                g2d.drawString(cs, posx, posy);
                ++c;
            }
        }
        buffer = ImageUtils.imageToBuffer(image);
        charHeight = ch;
        return Result.ready();
    }

    @Override
    protected final FontBitmap createAsset() {
        return new FontBitmap(buffer, width, charHeight, charWidth);
    }
}
