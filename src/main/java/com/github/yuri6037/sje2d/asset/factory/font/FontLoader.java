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

import com.github.yuri6037.sje2d.asset.Font;
import com.github.yuri6037.sje2d.asset.engine.AssetURL;
import com.github.yuri6037.sje2d.asset.engine.AssetURLBuilder;
import com.github.yuri6037.sje2d.asset.engine.VirtualPathBuilder;
import com.github.yuri6037.sje2d.asset.engine.map.AssetDepMap;
import com.github.yuri6037.sje2d.asset.engine.map.AssetStore;
import com.github.yuri6037.sje2d.asset.engine.system.ITAssetLoader;
import com.github.yuri6037.sje2d.asset.engine.system.stream.IAssetStream;
import com.github.yuri6037.sje2d.asset.engine.system.stream.StreamUtils;
import com.github.yuri6037.sje2d.config.FontType;
import com.github.yuri6037.sje2d.config.RuleType;
import com.github.yuri6037.sje2d.config.SystemType;
import com.github.yuri6037.sje2d.util.MathUtils;

import javax.xml.bind.JAXBContext;
import javax.xml.transform.stream.StreamSource;
import java.io.InputStream;
import java.util.ArrayList;

public final class FontLoader implements ITAssetLoader<Font> {
    private final InputStream stream;
    private String vpath;
    private int bitmapWidth;
    private final ArrayList<Rule> rules = new ArrayList<>();
    private AssetURL baseUrl;
    private final AssetURL url;

    /**
     * Creates a new FontLoader.
     * @param url the asset url.
     * @param stream the asset stream to load the font from.
     */
    public FontLoader(final AssetURL url, final IAssetStream stream) {
        this.stream = StreamUtils.makeInputStream(stream);
        this.url = url;
    }

    private AssetURL loadRuleContent(final SystemType system, final String url1) throws Exception {
        if (url1 != null) {
            return new AssetURL("font-bitmap/ttf", url1);
        } else if (system != null) {
            return new AssetURLBuilder().protocol("none").mimeType("font-bitmap/system").path(system.getFamily())
                    .parameter("size", system.getSize())
                    .parameter("bold", system.getBold())
                    .parameter("italic", system.getItalic())
                    .build();
        } else {
            throw new IllegalArgumentException("Invalid rule type");
        }
    }

    @Override
    public Result load(final AssetDepMap dependencies) throws Exception {
        JAXBContext ctx = JAXBContext.newInstance(FontType.class);
        FontType font = ctx.createUnmarshaller().unmarshal(new StreamSource(stream), FontType.class).getValue();
        vpath = new VirtualPathBuilder(url).setType("Font").setPath(font.getName()).build();
        bitmapWidth = Integer.parseInt(font.getWidth());
        if (!MathUtils.isPowerOfTwo(bitmapWidth)) {
            throw new IllegalArgumentException("Font bitmap width must be a power of 2");
        }
        baseUrl = loadRuleContent(font.getBase().getSystem(), font.getBase().getUrl());
        for (RuleType rule: font.getRules().getRule()) {
            int min = Integer.parseInt(rule.getMin(), 16);
            int max = Integer.parseInt(rule.getMax(), 16);
            AssetURL url1 = loadRuleContent(rule.getSystem(), rule.getUrl());
            rules.add(new Rule(min, max, url1));
        }
        return Result.ready();
    }

    @Override
    public AssetStore<Font> create() throws Exception {
        return new AssetStore<>(vpath, new Font(vpath, bitmapWidth, rules, baseUrl));
    }
}
