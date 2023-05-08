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

package com.github.yuri6037.sje2d.asset.factory.animation;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import javax.imageio.ImageIO;
import javax.xml.bind.JAXBContext;
import javax.xml.transform.stream.StreamSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.yuri6037.sje2d.asset.engine.AssetURL;
import com.github.yuri6037.sje2d.asset.engine.system.stream.IAssetStream;
import com.github.yuri6037.sje2d.asset.engine.system.stream.StreamUtils;
import com.github.yuri6037.sje2d.config.AnimationType;

public final class AnimationZIPLoader extends AnimationLoader {
    private static final Logger LOGGER = LoggerFactory.getLogger(AnimationZIPLoader.class);
    private final InputStream stream;

    /**
     * Creates a new animation ZIP loader.
     * @param stream the asset stream.
     * @param url the asset url.
     */
    public AnimationZIPLoader(final IAssetStream stream, final AssetURL url) {
        super(url);
        this.stream = StreamUtils.makeInputStream(stream);
    }

    private AnimationType loadDescriptor(final byte[] buffer) throws Exception {
        JAXBContext ctx = JAXBContext.newInstance(AnimationType.class);
        AnimationType animation = ctx.createUnmarshaller().unmarshal(new StreamSource(new ByteArrayInputStream(buffer)),
            AnimationType.class).getValue();
        return animation;
    }

    private void loadAnimationFrames(final AnimationType animation,
        final HashMap<String, byte[]> items) throws IOException {
        for (String frame : animation.getFrames().getFrame()) {
            ByteArrayInputStream s = new ByteArrayInputStream(items.get(frame));
            frames.add(ImageIO.read(s));
        }
    }

    @Override
    protected void build() throws Exception {
        ZipInputStream zip =  new ZipInputStream(stream);
        HashMap<String, byte[]> items = new HashMap<>();
        AnimationType animation = null;
        ZipEntry entry = null;
        do {
            entry = zip.getNextEntry();
            if (entry == null || entry.getSize() <= 0) {
                continue;
            }
            LOGGER.debug("New ZIP entry: {}", entry.getName());
            byte[] buffer = new byte[(int) entry.getSize()];
            zip.read(buffer);
            if ("animation.xml".equals(entry.getName())) {
                animation = loadDescriptor(buffer);
            } else {
                items.put(entry.getName(), buffer);
            }
        } while (entry != null);
        if (animation != null) {
            loadAnimationFrames(animation, items);
            frameWidth = animation.getWidth().intValue();
            frameHeight = animation.getHeight().intValue();
            fps = animation.getFps().intValue();
        }
    }
}
