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

import java.nio.ByteBuffer;
import java.util.HashMap;

public class FontBitmap extends Texture {
    private final HashMap<Integer, Integer> charWidth;
    private final int charHeight;

    /**
     * Creates a new texture from a buffer and its size.
     * @param buffer the buffer containing all texel data.
     * @param width the bitmap width.
     * @param charHeight the maximum character height of this font bitmap.
     * @param charWidth the width of each character in this font bitmap.
     */
    public FontBitmap(final ByteBuffer buffer, final int width, final int charHeight,
                      final HashMap<Integer, Integer> charWidth) {
        //noinspection SuspiciousNameCombination
        super(buffer, width, width);
        this.charHeight = charHeight;
        this.charWidth = charWidth;
    }

    /**
     * @return the maximum character height of this font bitmap.
     */
    public int getHeight() {
        return charHeight;
    }

    /**
     * Gets the width of a character.
     * @param c the character to get the width of.
     * @return the width of the character or -1 if the character does not exist.
     */
    public int getWidth(final int c) {
        Integer i = charWidth.get(c);
        return i == null ? -1 : i;
    }
}
