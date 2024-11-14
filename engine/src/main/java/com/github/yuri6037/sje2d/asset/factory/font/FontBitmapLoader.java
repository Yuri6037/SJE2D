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

package com.github.yuri6037.sje2d.asset.factory.font;

import com.github.yuri6037.sje2d.asset.FontBitmap;
import com.github.yuri6037.sje2d.asset.engine.AssetURL;
import com.github.yuri6037.sje2d.asset.engine.map.AssetDepMap;
import com.github.yuri6037.sje2d.asset.engine.map.AssetStore;
import com.github.yuri6037.sje2d.asset.engine.system.ITAssetLoader;
import com.github.yuri6037.sje2d.math.MathUtils;
import com.github.yuri6037.sje2d.util.ImageUtils;
import com.github.yuri6037.sje2d.util.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Color;
import java.awt.image.BufferedImage;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.util.HashMap;

//CHECKSTYLE OFF: AvoidStarImport
import static org.lwjgl.opengl.GL12.*;
//CHECKSTYLE ON

public abstract class FontBitmapLoader implements ITAssetLoader<FontBitmap> {
    private static final Logger LOGGER = LoggerFactory.getLogger(FontBitmapLoader.class);

    //CHECKSTYLE OFF: VisibilityModifier
    /**
     * The AssetURL that is being loaded.
     */
    protected final AssetURL url;
    //CHECKSTYLE ON

    /**
     * Creates a new FontBitmapLoader.
     * @param url the asset url that is going to be loaded.
     */
    public FontBitmapLoader(final AssetURL url) {
        this.url = url;
    }

    /**
     * Builds the font object used to render the bitmap.
     * @return a new Font object.
     * @throws Exception if the font failed to build.
     */
    protected abstract Font buildFont() throws Exception;

    private int width;
    private String vpath;
    private int charHeight;
    private final HashMap<Integer, Integer> charWidth = new HashMap<>();
    private ByteBuffer buffer;
    private int guessBearingX;
    private int descent;

    /**
     * @return the expected size in points of each character in the rendered bitmap.
     */
    protected int getFontSize() {
        return Integer.parseInt(url.getParameter("size", "12"));
    }

    /**
     * @return the expected style of each character in the rendered bitmap.
     */
    protected int getFontStyle() {
        boolean bold = StringUtils.parseBoolean(url.getParameter("bold"));
        boolean italic = StringUtils.parseBoolean(url.getParameter("italic"));
        int style = Font.PLAIN;
        if (bold) {
            style |= Font.BOLD;
        }
        if (italic) {
            style |= Font.ITALIC;
        }
        return style;
    }

    @Override
    public final Result load(final AssetDepMap dependencies) throws Exception {
        width = Integer.parseInt(url.getParameter("width", "512"));
        if (!MathUtils.isPowerOfTwo(width)) {
            throw new IllegalArgumentException("Bitmap size is not a power of 2");
        }
        int blockSize =  width / 16;
        int plane = Integer.parseInt(url.getParameter("plane", "0"));
        String baseVpath = url.getParameter("vpath", "Font/Generic");
        vpath = baseVpath + "/Plane" + plane;
        Font font = buildFont();
        LOGGER.debug("Building font bitmap ({}x{} - {}) for plane #{}...", width, width, blockSize,
                plane);
        if (plane * 256 > 1114112) {
            throw new IllegalArgumentException("Character plane is out of range");
        }
        @SuppressWarnings("SuspiciousNameCombination")
        BufferedImage image = new BufferedImage(width, width, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = image.createGraphics();
        g2d.setFont(font);
        g2d.setColor(Color.WHITE);
        // Unfortunately it appears that in java, getting the X bearing of a font is impossible.
        guessBearingX = getFontSize() / 4;
        descent = g2d.getFontMetrics().getDescent();
        int ch = g2d.getFontMetrics().getHeight();
        int c = plane * 256;
        for (int i = 0; i != 16; ++i) {
            for (int j = 0; j != 16; ++j) {
                byte[] arr = ByteBuffer.allocate(4).putInt(c).array();
                String cs = new String(arr, Charset.forName("UTF-32"));
                int cw = g2d.getFontMetrics().charWidth(c);
                charWidth.put(c, cw);
                int posx = j * blockSize;
                int posy = i * blockSize;
                //g2d.drawRect(posx, posy, blockSize, blockSize);
                g2d.drawString(cs, posx + guessBearingX, (posy + blockSize) - descent);
                ++c;
            }
        }
        //ImageIO.write(image, "png", new File("./" + vpath.replace("/", "_") + ".png"));
        buffer = ImageUtils.imageToBuffer(image);
        charHeight = ch;
        return Result.ready();
    }

    @Override
    public final AssetStore<FontBitmap> create() throws Exception {
        FontBitmap bitmap = new FontBitmap(buffer, width, charHeight, guessBearingX, descent, charWidth);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_CLAMP_TO_EDGE);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_CLAMP_TO_EDGE);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST);
        return new AssetStore<>(vpath, bitmap);
    }
}
