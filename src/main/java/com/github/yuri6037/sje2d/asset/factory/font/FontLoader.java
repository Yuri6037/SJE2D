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
import com.github.yuri6037.sje2d.util.MathUtils;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.namespace.QName;
import javax.xml.transform.stream.StreamSource;
import java.io.InputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public final class FontLoader implements ITAssetLoader<Font> {
    private final InputStream stream;
    private String vpath;
    private int bitmapWidth;
    private final ArrayList<Rule> rules = new ArrayList<>();
    private AssetURL baseUrl;
    private final AssetURL url;

    private static final QName RULE_FAMILY_QNAME = new QName("sje2d", "family");
    private static final QName RULE_SIZE_QNAME = new QName("sje2d", "size");
    private static final QName RULE_ITALIC_QNAME = new QName("sje2d", "italic");
    private static final QName RULE_BOLD_QNAME = new QName("sje2d", "bold");

    /**
     * Creates a new FontLoader.
     * @param url the asset url.
     * @param stream the asset stream to load the font from.
     */
    public FontLoader(final AssetURL url, final IAssetStream stream) {
        this.stream = StreamUtils.makeInputStream(stream);
        this.url = url;
    }

    private AssetURL loadRuleContent(final String type, final List<Serializable> lst) throws Exception {
        if ("url".equals(type)) {
            if (lst.size() != 1) {
                throw new IllegalArgumentException("Invalid rule format");
            }
            String s = (String) lst.get(0);
            return new AssetURL("font-bitmap/ttf", s);
        } else if ("system".equals(type)) {
            AssetURLBuilder builder = new AssetURLBuilder();
            builder.protocol("none").mimeType("font-bitmap/system");
            for (Serializable item : lst) {
                @SuppressWarnings("unchecked") JAXBElement<String> elem = (JAXBElement<String>) item;
                if (RULE_FAMILY_QNAME.equals(elem.getName())) {
                    builder.path(elem.getValue());
                } else if (RULE_SIZE_QNAME.equals(elem.getName())) {
                    builder.parameter("size", elem.getValue());
                } else if (RULE_BOLD_QNAME.equals(elem.getName())) {
                    builder.parameter("bold", elem.getValue());
                } else if (RULE_ITALIC_QNAME.equals(elem.getName())) {
                    builder.parameter("italic", elem.getValue());
                }
            }
            return builder.build();
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
        baseUrl = loadRuleContent(font.getBase().getType(), font.getBase().getContent());
        for (RuleType rule: font.getRules().getRule()) {
            int min = Integer.parseInt(rule.getMin());
            int max = Integer.parseInt(rule.getMax());
            AssetURL url1 = loadRuleContent(rule.getType(), rule.getContent());
            rules.add(new Rule(min, max, url1));
        }
        return Result.ready();
    }

    @Override
    public AssetStore<Font> create() throws Exception {
        return new AssetStore<>(vpath, new Font(vpath, bitmapWidth, rules, baseUrl));
    }
}
