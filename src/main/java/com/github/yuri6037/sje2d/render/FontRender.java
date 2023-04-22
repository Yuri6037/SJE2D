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

package com.github.yuri6037.sje2d.render;

import com.github.yuri6037.sje2d.asset.Font;
import com.github.yuri6037.sje2d.asset.FontBitmap;
import com.github.yuri6037.sje2d.asset.engine.AssetURL;
import com.github.yuri6037.sje2d.asset.engine.manager.AssetManagerProxy;
import com.github.yuri6037.sje2d.asset.engine.map.AssetStore;
import com.github.yuri6037.sje2d.util.UTF32Str;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;

//CHECKSTYLE OFF: AvoidStarImport
import static org.lwjgl.opengl.GL11.*;
//CHECKSTYLE ON

public final class FontRender {
    private final AssetStore<Font>.Ref font;

    private final HashMap<Integer, AssetStore<FontBitmap>.Ref> bitmaps = new HashMap<>();
    private final HashSet<AssetURL> queuedBitmaps = new HashSet<>();

    /**
     * Creates a new FontRender.
     * @param font the font to use for drawing text.
     */
    public FontRender(final AssetStore<Font>.Ref font) {
        this.font = font;
    }

    /**
     * Determines whether a string can be fully rendered without further access to the asset system.
     * @param text the target string to verify.
     * @return true if the bitmaps needed to draw the string are in memory, false otherwise.
     */
    public boolean isLoaded(final UTF32Str text) {
        Iterator<Integer> iter = text.iterator();
        while (iter.hasNext()) {
            int plane = font.get().getPlane(iter.next());
            if (!bitmaps.containsKey(plane)) {
                return false;
            }
        }
        return true;
    }

    private AssetStore<FontBitmap>.Ref getBitmap(final AssetManagerProxy assets, final int c) {
        int plane = font.get().getPlane(c);
        if (!bitmaps.containsKey(plane)) {
            AssetStore<FontBitmap>.Ref bitmap = assets.get(FontBitmap.class, font.get().getVirtualPath(plane));
            if (bitmap == null) {
                AssetURL url = font.get().getURL(c);
                if (!queuedBitmaps.contains(url)) {
                    assets.queue(url);
                    queuedBitmaps.add(url);
                }
                return null;
            } else {
                bitmaps.put(plane, bitmap);
            }
        }
        return bitmaps.get(plane);
    }

    /**
     * Queues all bitmaps needed to render the given text.
     * @param assets a pointer to the asset system to queue bitmap assets on demand.
     * @param text the target string.
     * @return true if some bitmaps where queued, false otherwise.
     */
    public boolean loadString(final AssetManagerProxy assets, final UTF32Str text) {
        boolean queued = false;
        Iterator<Integer> iter = text.iterator();
        while (iter.hasNext()) {
            int c = iter.next();
            if (getBitmap(assets, c) == null) {
                queued = true;
            }
        }
        return queued;
    }

    /**
     * Gets the size of a given string.
     * @param assets a pointer to the asset system to queue bitmap assets on demand.
     * @param text the target string to compute the size of.
     * @return the size of the string in text.
     */
    public Size getStringSize(final AssetManagerProxy assets, final UTF32Str text) {
        float width = 0;
        float height = 0;
        Iterator<Integer> iter = text.iterator();
        while (iter.hasNext()) {
            int c = iter.next();
            AssetStore<FontBitmap>.Ref bitmap = getBitmap(assets, c);
            if (bitmap == null) {
                continue;
            }
            int cHeight = bitmap.get().getHeight();
            int cWidth = bitmap.get().getWidth(c);
            width += cWidth;
            if (cHeight > height) {
                height = (float) cHeight;
            }
        }
        return new Size(width, height);
    }

    /**
     * Draws a string on the screen.
     * @param assets a pointer to the asset system to queue bitmap assets on demand.
     * @param text the target string to render.
     * @param x x coordinate.
     * @param y y coordinate.
     * @return true if the string was rendered, false if some missing font bitmaps have been queued.
     */
    public boolean drawString(final AssetManagerProxy assets, final UTF32Str text, final float x, final float y) {
        float blockSize = (float) font.get().getBlockSize();
        glEnable(GL_TEXTURE_2D);
        boolean queued = false;
        Iterator<Integer> iter = text.iterator();
        float posx = x;
        while (iter.hasNext()) {
            int c = iter.next();
            AssetStore<FontBitmap>.Ref bitmap = getBitmap(assets, c);
            if (bitmap == null) {
                queued = true;
                continue;
            }
            glBindTexture(GL_TEXTURE_2D, bitmap.get().getGLId());
            int width = bitmap.get().getWidth(c);
            int cPlane = c % 256;
            int gx = (cPlane % 16);
            int gy = (cPlane - gx) / 16;
            float u = gx / 16f;
            float v = gy / 16f;
            //1/17 because somehow OpenGL has a bug and believes that 16 ~= 16 + 2 (WTF?!)
            float u1 = u + 1 / 17f;
            float v1 = v + 1 / 17f;

            glBegin(GL_QUADS);
            glTexCoord2f(u, v);
            glVertex2f(posx, y);
            glTexCoord2f(u1, v);
            glVertex2f(posx + blockSize, y);
            glTexCoord2f(u1, v1);
            glVertex2f(posx + blockSize, y + blockSize);
            glTexCoord2f(u, v1);
            glVertex2f(posx, y + blockSize);
            glEnd();

            posx += (float) width;
        }
        return queued;
    }
}
