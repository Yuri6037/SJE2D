/*
 * Copyright (c) 2024, SJE2D
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

package com.github.yuri6037.sje2d.ui.asset.factory;

import com.github.yuri6037.sje2d.asset.engine.AssetURL;
import com.github.yuri6037.sje2d.asset.engine.system.stream.IAssetStream;
import com.github.yuri6037.sje2d.asset.factory.base.XmlReflectionLoader;
import com.github.yuri6037.sje2d.reflect.ClassRegistry;
import com.github.yuri6037.sje2d.ui.asset.style.Style;
import com.github.yuri6037.sje2d.ui.asset.style.CompositeStyle;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

public final class StyleLoader extends XmlReflectionLoader<Style> {
    /**
     * The registry of all style classes.
     */
    public static final ClassRegistry STYLE_REGISTRY = new ClassRegistry();

    private Style style;

    /**
     * Creates a new Style XML loader.
     * @param url the url of the asset to load.
     * @param stream the stream to read from.
     */
    public StyleLoader(final AssetURL url, final IAssetStream stream) {
        super(url, stream, STYLE_REGISTRY);
        addHandler("id", null);
    }

    private Style create(final Element element) throws Exception {
        Style style1 = createObject(Style.class, element);
        configureObject(element, style1);
        if (style1 instanceof CompositeStyle) {
            for (int i = 0; i != element.getChildNodes().getLength(); ++i) {
                Node node = element.getChildNodes().item(i);
                if (node instanceof Element) {
                    String name = ((Element) node).getAttribute("id");
                    ((CompositeStyle) style1).add(name, create((Element) node));
                }
            }
        }
        return style1;
    }

    @Override
    protected void loadDocument(final Document document) throws Exception {
        Element element = document.getDocumentElement();
        style = create(element);
    }

    @Override
    protected Style createAsset() throws Exception {
        return style;
    }

    static {
        STYLE_REGISTRY.addSearchPath("com.github.yuri6037.sje2d.ui.asset.style");
    }
}
