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

import com.github.yuri6037.sje2d.asset.Strings;
import com.github.yuri6037.sje2d.asset.engine.AssetURL;
import com.github.yuri6037.sje2d.asset.engine.system.stream.IAssetStream;
import com.github.yuri6037.sje2d.asset.factory.base.XmlReflectionLoader;
import com.github.yuri6037.sje2d.reflect.ClassRegistry;
import com.github.yuri6037.sje2d.ui.asset.Layout;
import com.github.yuri6037.sje2d.ui.asset.Theme;
import com.github.yuri6037.sje2d.ui.component.Component;
import com.github.yuri6037.sje2d.ui.panel.IPanel;
import com.github.yuri6037.sje2d.util.UTF32Str;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

import java.util.HashMap;

public final class LayoutLoader extends XmlReflectionLoader<Layout> {
    /**
     * The registry of all component classes.
     */
    public static final ClassRegistry COMPONENT_REGISTRY = new ClassRegistry();

    private Strings strings = null;
    private Theme theme = null;
    private Component rootComponent = null;
    private final HashMap<String, Component> componentsById = new HashMap<>();

    private static final Logger LOGGER = LoggerFactory.getLogger(LayoutLoader.class);

    /**
     * Creates a new instance of a Layout XML loader.
     * @param url the url of the asset to load.
     * @param stream the stream to read from.
     */
    public LayoutLoader(final AssetURL url, final IAssetStream stream) {
        super(url, stream, COMPONENT_REGISTRY);
        addHandler("id", (node, obj) -> {
            ((Component) obj).setId(node.getNodeValue());
            componentsById.put(node.getNodeValue(), (Component) obj);
        });
        addHandler("strings", null);
        addHandler("theme", null);
        addDecoder(UTF32Str.class, (value) -> strings.get(value));
    }

    @Override
    protected Layout createAsset() {
        return new Layout(rootComponent, componentsById);
    }

    private Component create(final Element element) throws Exception {
        Component obj = createObject(Component.class, element);
        configureObject(element, obj);
        if (obj instanceof IPanel) {
            for (int i = 0; i != element.getChildNodes().getLength(); ++i) {
                Node node = element.getChildNodes().item(i);
                if (node instanceof Element) {
                    ((IPanel) obj).add(create((Element) node));
                }
            }
        }
        if (theme != null) {
            obj.applyDefaultStyle(theme);
        }
        return obj;
    }

    @Override
    protected void loadDocument(final Document document) throws Exception {
        Element root = document.getDocumentElement();
        String vpath = root.hasAttribute("strings") ? root.getAttribute("strings") : null;
        if (vpath != null) {
            LOGGER.info("Loading strings asset...");
            strings = awaitAsset(Strings.class, vpath);
            if (strings == null) {
                throw new ClassCastException("Attempt to reference non strings asset in Layout");
            }
        }
        vpath = root.hasAttribute("theme") ? root.getAttribute("theme") : null;
        if (vpath != null) {
            LOGGER.info("Loading base theme asset...");
            theme = awaitAsset(Theme.class, vpath);
            if (strings == null) {
                throw new ClassCastException("Attempt to reference non theme asset in Layout");
            }
        }
        rootComponent = create(root);
    }

    static {
        COMPONENT_REGISTRY.addSearchPath("com.github.yuri6037.sje2d.ui.component");
        COMPONENT_REGISTRY.addSearchPath("com.github.yuri6037.sje2d.ui.panel");
    }
}
