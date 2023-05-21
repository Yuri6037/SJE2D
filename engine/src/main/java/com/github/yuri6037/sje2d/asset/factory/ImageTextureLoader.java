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

package com.github.yuri6037.sje2d.asset.factory;

import com.github.yuri6037.sje2d.asset.Texture;
import com.github.yuri6037.sje2d.asset.engine.AssetURL;
import com.github.yuri6037.sje2d.asset.engine.map.AssetDepMap;
import com.github.yuri6037.sje2d.asset.engine.system.stream.IAssetStream;
import com.github.yuri6037.sje2d.asset.engine.system.stream.StreamUtils;
import com.github.yuri6037.sje2d.math.MathUtils;
import com.github.yuri6037.sje2d.util.ImageUtils;
import com.github.yuri6037.sje2d.util.StringEnum;
import org.lwjgl.opengl.GL12;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.nio.ByteBuffer;

//CHECKSTYLE OFF: AvoidStarImport
import static org.lwjgl.opengl.GL11.*;
//CHECKSTYLE ON

public final class ImageTextureLoader extends BaseLoader<Texture> {
    private static final Logger LOGGER = LoggerFactory.getLogger(ImageTextureLoader.class);

    private static final StringEnum<Integer> WRAP_MODE = StringEnum.create(
            "edgeclamp", GL12.GL_CLAMP_TO_EDGE,
            "clamp", GL_CLAMP,
            "repeat", GL_REPEAT
    );

    private static final StringEnum<Integer> SAMPLE_MODE = StringEnum.create(
            "nearest", GL_NEAREST,
            "linear", GL_LINEAR
    );

    private final IAssetStream stream;
    private ByteBuffer buffer;
    private int width;
    private int height;
    private int xWrap;
    private int yWrap;
    private int min;
    private int mag;

    ImageTextureLoader(final AssetURL url, final IAssetStream stream) {
        super(url);
        this.stream = stream;
    }

    private void computeModes() {
        xWrap = WRAP_MODE.get(GL12.GL_CLAMP_TO_EDGE, url.getParameter("xwrap"));
        yWrap = WRAP_MODE.get(GL12.GL_CLAMP_TO_EDGE, url.getParameter("ywrap"));
        min = SAMPLE_MODE.get(GL_NEAREST, url.getParameter("min"));
        mag = SAMPLE_MODE.get(GL_NEAREST, url.getParameter("mag"));
        LOGGER.debug("X-wrap mode: {}, Y-wrap mode: {}, min sample mode: {}, mag sample mode: {}", xWrap, yWrap,
                min, mag);
    }

    @Override
    public Result load(final AssetDepMap dependencies) throws Exception {
        BufferedImage image = ImageIO.read(StreamUtils.makeInputStream(stream));
        if (!MathUtils.isPowerOfTwo(image.getWidth()) || !MathUtils.isPowerOfTwo(image.getHeight())) {
            throw new IllegalArgumentException("Image size is not a power of 2");
        }
        buffer = ImageUtils.imageToBuffer(image);
        width = image.getWidth();
        height = image.getHeight();
        computeModes();
        return Result.ready();
    }

    @Override
    protected Texture createAsset() {
        Texture texture = new Texture(buffer, width, height);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, xWrap);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, yWrap);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, min);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, mag);
        return texture;
    }
}
