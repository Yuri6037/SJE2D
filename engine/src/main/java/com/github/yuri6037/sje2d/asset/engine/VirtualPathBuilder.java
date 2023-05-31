/*
 * Copyright (c) 2023, SJE2D
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

package com.github.yuri6037.sje2d.asset.engine;

import com.github.yuri6037.sje2d.util.StringUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;

public final class VirtualPathBuilder {
    private final AssetURL url;
    private ArrayList<String> components;
    private String namespace;
    private String providedType;

    /**
     * Creates a new VirtualPathBuilder.
     * @param url the base AssetURL to start from.
     */
    public VirtualPathBuilder(final AssetURL url) {
        this.url = url;
        namespace = url.getParameter("namespace");
        providedType = guessAssetType();
        guessVirtualPath();
    }

    private String guessAssetType() {
        if (url.getMimeType() == null) {
            return "Untyped";
        }
        int id = url.getMimeType().indexOf('/');
        if (id != -1) {
            return StringUtils.capitalize(url.getMimeType().substring(0, id));
        } else {
            return StringUtils.capitalize(url.getMimeType());
        }
    }

    private void guessVirtualPath() {
        String vpath = url.getParameter("vpath");
        //If we have an explicit vpath specified, return it.
        if (vpath != null) {
            components = new ArrayList<>(Arrays.asList(vpath.split("/")));
            return;
        }
        //If no explicit vpath was specified, try to guess one.
        String[] split = url.getPath().split("/");
        //Compute the inverse list of path components to compose the guessed virtual path.
        components = new ArrayList<>();
        String name = split[split.length - 1];
        int id = name.indexOf('.');
        if (id != -1) {
            components.add(name.substring(0, id));
        } else {
            components.add(name);
        }
        for (int i = split.length - 2; i > 0; --i) {
            if (!providedType.equalsIgnoreCase(split[i])) {
                components.add(split[i]);
            } else {
                break;
            }
        }
        //Reverse the components list
        Collections.reverse(components);
    }

    //CHECKSTYLE OFF: HiddenField
    /**
     * Replaces the namespace component of this virtual path.
     * @param namespace the new namespace component.
     * @return this.
     */
    public VirtualPathBuilder setNamespace(final String namespace) {
        this.namespace = namespace;
        return this;
    }
    //CHECKSTYLE ON

    /**
     * Replaces the type component of this virtual path.
     * @param type the new type component.
     * @return this.
     */
    public VirtualPathBuilder setType(final String type) {
        providedType = type;
        if (providedType != null) {
            guessVirtualPath();
        }
        return this;
    }

    /**
     * Replaces all path components (excluding namespace and type).
     * @param path the new replacement path.
     * @return this.
     */
    public VirtualPathBuilder setPath(final String path) {
        components = new ArrayList<>(Arrays.asList(path.split("/")));
        return this;
    }

    /**
     * Appends a component or a path to this virtual path.
     * @param component the component or path to append.
     * @return this.
     */
    public VirtualPathBuilder append(final String component) {
        if (component.contains("/")) {
            components.addAll(Arrays.asList(component.split("/")));
        } else {
            components.add(component);
        }
        return this;
    }

    /**
     * Limits the number of path components (excluding namespace and type).
     * @param maxComponents the maximum number of path components.
     * @return this.
     */
    public VirtualPathBuilder limit(final int maxComponents) {
        if (components.size() <= maxComponents) {
            return this;
        }
        int remaining = components.size() - maxComponents;
        while (remaining > 0) {
            components.remove(0);
            --remaining;
        }
        return this;
    }

    /**
     * @return an iterator over the virtual path components (excluding namespace and type).
     */
    public Iterator<String> getPath() {
        return components.iterator();
    }

    /**
     * Builds the corresponding asset virtual path.
     * NOTE: It is undefined to re-use the same builder once build has been called.
     * @return the new virtual path.
     */
    public String build() {
        if (providedType != null) {
            components.add(0, providedType);
        }
        if (namespace != null) {
            components.add(0, namespace);
        }
        String res = String.join("/", components);
        components = null;
        providedType = null;
        return res;
    }
}
