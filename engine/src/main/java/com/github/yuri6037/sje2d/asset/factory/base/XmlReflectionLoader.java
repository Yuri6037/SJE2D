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

package com.github.yuri6037.sje2d.asset.factory.base;

import com.github.yuri6037.sje2d.asset.Font;
import com.github.yuri6037.sje2d.asset.Texture;
import com.github.yuri6037.sje2d.asset.engine.AssetURL;
import com.github.yuri6037.sje2d.asset.engine.system.IAsset;
import com.github.yuri6037.sje2d.asset.engine.system.stream.IAssetStream;
import com.github.yuri6037.sje2d.asset.engine.system.stream.StreamUtils;
import com.github.yuri6037.sje2d.reflect.ClassRegistry;
import com.github.yuri6037.sje2d.reflect.Configurator;
import com.github.yuri6037.sje2d.reflect.IConfigurable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.InputStream;
import java.util.HashMap;
import java.util.function.Function;

public abstract class XmlReflectionLoader<T extends IAsset> extends AsyncLoader<T> {
    private static final Logger LOGGER = LoggerFactory.getLogger(XmlReflectionLoader.class);

    private final InputStream stream;
    private Document document1 = null;
    private final HashMap<String, IXmlFunction> handlersByName = new HashMap<>();
    private final HashMap<Class<?>, Function<String, Object>> parameterDecoders = new HashMap<>();

    /**
     * Create a new instance of a loader which loads types registered with the reflection engine described in XML.
     * @param url the url to load.
     * @param stream the XML file stream.
     */
    public XmlReflectionLoader(final AssetURL url, final IAssetStream stream) {
        super(url);
        this.stream = StreamUtils.makeInputStream(stream);
    }

    /**
     * Add a XML node handler.
     * @param targetNodeName the name of the node to attach to.
     * @param handler the handler function.
     */
    protected final void addHandler(final String targetNodeName, final IXmlFunction handler) {
        handlersByName.put(targetNodeName, handler);
    }

    /**
     * Adds a custom parameter decoder.
     * @param paramType the type class of the parameter.
     * @param decoder the function to decode and return an instance of the parameter value.
     */
    protected final void addDecoder(final Class<?> paramType, final Function<String, Object> decoder) {
        parameterDecoders.put(paramType, decoder);
    }

    /**
     * Creates an object from a XML element using the reflection engine.
     * @param objectClass the object class to cast to.
     * @param element the XML element containing the object name and properties.
     * @return a new instance of the created object.
     * @param <V> the generic object class to cast to.
     * @throws Exception when the object could not be created.
     */
    protected <V extends IConfigurable> V createObject(final Class<V> objectClass, final Element element)
            throws Exception {
        Class<? extends IConfigurable> cl = ClassRegistry.getClass(element.getTagName());
        IConfigurable obj = cl.getConstructor().newInstance();
        return objectClass.cast(obj);
    }

    /**
     * Configures an object from a XML element.
     * @param element the XML element holding the configuration to apply.
     * @param obj the object to configure.
     * @throws IllegalArgumentException when one or more parameters could not be decoded or set properly.
     */
    protected void configureObject(final Element element, final IConfigurable obj) throws IllegalArgumentException {
        for (int i = 0; i != element.getAttributes().getLength(); ++i) {
            Node node = element.getAttributes().item(i);
            LOGGER.debug("Parsing attribute: {}", node.getNodeName());
            if (handlersByName.containsKey(node.getNodeName())) {
                LOGGER.debug("Attribute is bound to a handler...");
                IXmlFunction func = handlersByName.get(node.getNodeName());
                if (func != null) {
                    func.configure(node, obj);
                }
                continue;
            }
            final Class<?> paramType = obj.getParamType(node.getNodeName());
            LOGGER.debug("Attribute {} has type {}", node.getNodeName(), paramType);
            if (parameterDecoders.containsKey(paramType)) {
                obj.setParam(node.getNodeName(), parameterDecoders.get(paramType).apply(node.getNodeValue()));
            } else if (paramType == Font.class) {
                obj.setParam(node.getNodeName(), awaitAsset(Font.class, node.getNodeValue()));
            } else if (paramType == Texture.class) {
                obj.setParam(node.getNodeName(), awaitAsset(Texture.class, node.getNodeValue()));
            } else {
                Object value = Configurator.parsePrimitive(paramType, node.getNodeValue());
                if (value != null) {
                    obj.setParam(node.getNodeName(), value);
                } else {
                    throw new IllegalArgumentException("Unable to parse primitive value for parameter name: "
                            + node.getNodeName());
                }
            }
        }
    }

    @Override
    protected final void loadAsync() throws Exception {
        if (document1 == null) {
            DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
            document1 = builder.parse(stream);
            stream.close();
        }
        loadDocument(document1);
    }

    /**
     * Loads this asset from a XML document.
     * @param document the document to load from.
     * @throws Exception if the asset failed to load.
     */
    protected abstract void loadDocument(Document document) throws Exception;

    @Override
    protected abstract T createAsset() throws Exception;
}
