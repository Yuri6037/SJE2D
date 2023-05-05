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

package com.github.yuri6037.sje2d.util;

import java.awt.image.BufferedImage;
import java.nio.ByteBuffer;

public final class ImageUtils {
    private ImageUtils() {
    }

    /**
     * Converts a Java BufferedImage to a contiguous array of texels in RGBA format.
     * @param image the image to convert.
     * @return the newly allocated ByteBuffer.
     */
    public static ByteBuffer imageToBuffer(final BufferedImage image) {
        ByteBuffer buffer = ByteBuffer.allocateDirect(image.getWidth() * image.getHeight() * 4);
        for (int x = 0; x != image.getWidth(); ++x) {
            for (int y = 0; y != image.getHeight(); ++y) {
                int argb = image.getRGB(x, y);
                int offset = y * image.getWidth() * 4 + x * 4;
                buffer.put(offset, (byte) ((argb >> 16) & 0xFF)); //R channel
                buffer.put(offset + 1, (byte) ((argb >> 8) & 0xFF)); //G channel
                buffer.put(offset + 2, (byte) (argb & 0xFF)); //B channel
                buffer.put(offset + 3, (byte) ((argb >> 24) & 0xFF)); //A channel
            }
        }
        return buffer;
    }
}
