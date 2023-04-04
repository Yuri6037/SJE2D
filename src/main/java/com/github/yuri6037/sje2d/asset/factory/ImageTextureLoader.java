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

package com.github.yuri6037.sje2d.asset.factory;

import com.github.yuri6037.sje2d.asset.Texture;
import com.github.yuri6037.sje2d.asset.engine.AssetURL;
import com.github.yuri6037.sje2d.asset.engine.map.AssetDepMap;
import com.github.yuri6037.sje2d.asset.engine.system.stream.IAssetStream;
import com.github.yuri6037.sje2d.asset.engine.system.stream.StreamUtils;
import com.github.yuri6037.sje2d.util.MathUtils;
import org.lwjgl.opengl.GL12;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.nio.ByteBuffer;

//CHECKSTYLE OFF: AvoidStarImport
import static org.lwjgl.opengl.GL11.*;
//CHECKSTYLE ON

public final class ImageTextureLoader extends BaseLoader<Texture> {
    private final IAssetStream stream;
    private ByteBuffer buffer;
    private int width;
    private int height;

    ImageTextureLoader(final AssetURL url, final IAssetStream stream) {
        super(url);
        this.stream = stream;
    }

    @Override
    public Result load(final AssetDepMap dependencies) throws Exception {
        BufferedImage image = ImageIO.read(StreamUtils.makeInputStream(stream));
        if (!MathUtils.isPowerOfTwo(image.getWidth()) || !MathUtils.isPowerOfTwo(image.getHeight())) {
            throw new IllegalArgumentException("Image size is not a power of 2");
        }
        buffer = ByteBuffer.allocateDirect(image.getWidth() * image.getHeight() * 4);
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
        width = image.getWidth();
        height = image.getHeight();
        return Result.ready();
    }

    @Override
    protected Texture createAsset() {
        glEnable(GL_TEXTURE_2D);
        int texture = glGenTextures();
        glBindTexture(GL_TEXTURE_2D, texture);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL12.GL_CLAMP_TO_EDGE);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL12.GL_CLAMP_TO_EDGE);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST);
        glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA, width, height, 0, GL_RGBA, GL_UNSIGNED_BYTE, buffer);
        glDisable(GL_TEXTURE_2D);
        return new Texture(texture);
    }
}
