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

import com.github.yuri6037.sje2d.asset.engine.AssetURL;
import com.github.yuri6037.sje2d.asset.engine.AssetURLBuilder;
import com.github.yuri6037.sje2d.asset.engine.system.IAsset;
import com.github.yuri6037.sje2d.asset.factory.font.Rule;

import java.net.MalformedURLException;
import java.util.ArrayList;

public final class Font implements IAsset {
    private final String basePath;
    private final int bitmapWidth;
    private final ArrayList<Rule> rules;
    private final AssetURL baseUrl;

    /**
     * Creates a new Font asset.
     * @param basePath the base virtual path of this font to load inner bitmaps.
     * @param bitmapWidth the width of the generated font bitmaps.
     * @param rules a list of rules to treat some Unicode ranges differently.
     * @param baseUrl the base URL if no rule exists for a given character range.
     */
    public Font(final String basePath, final int bitmapWidth, final ArrayList<Rule> rules, final AssetURL baseUrl) {
        this.bitmapWidth = bitmapWidth;
        this.rules = rules;
        this.baseUrl = baseUrl;
        this.basePath = basePath;
    }

    /**
     * @return the size of a single character block.
     */
    public int getBlockSize() {
        return bitmapWidth / 16;
    }

    /**
     * Gets the plane on which a character is located.
     * @param c the character to get the plane index of.
     * @return the plane index on which the character c is supposed to be rendered.
     */
    public int getPlane(final int c) {
        return c / 256;
    }

    /**
     * Gets the virtual path of a font bitmap based on the plane index.
     * @param plane the plane index to search for.
     * @return the corresponding asset virtual path for the given character plane.
     */
    public String getVirtualPath(final int plane) {
        return basePath + "/Plane" + plane;
    }

    /**
     * Gets the AssetURL which should be used to load the font bitmap which rasterize a given character.
     * @param c the target character to rasterize.
     * @return an instance of an AssetURL.
     */
    public AssetURL getURL(final int c) {
        AssetURL url = baseUrl;
        for (Rule r : rules) {
            if (c >= r.min() && c <= r.max()) {
                url = r.url();
                break;
            }
        }
        try {
            return new AssetURLBuilder(url)
                    .parameter("width", String.valueOf(bitmapWidth))
                    .parameter("plane", String.valueOf(getPlane(c)))
                    .parameter("vpath", basePath)
                    .build();
        } catch (MalformedURLException e) {
            //Normally this cannot occur.
            throw new RuntimeException(e);
        }
    }

    @Override
    public void unload() {
    }
}
