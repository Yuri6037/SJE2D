package com.github.yuri6037.sje2d.asset.factory.base;

import com.github.yuri6037.sje2d.reflect.IConfigurable;
import org.w3c.dom.Node;

public interface IXmlFunction {
    /**
     * Configures an object by a specific node. This interface is used by XmlReflectionLoader to customize the loader.
     * @param value the node.
     * @param obj the target object being configured.
     * @throws IllegalArgumentException when the object could not be configured.
     */
    void configure(Node value, IConfigurable obj) throws IllegalArgumentException;
}
