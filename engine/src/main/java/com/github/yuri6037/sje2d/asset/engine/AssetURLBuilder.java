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

package com.github.yuri6037.sje2d.asset.engine;

import java.net.MalformedURLException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class AssetURLBuilder {
    private String mimeType;
    private String protocol;
    private String path;
    private final HashMap<String, String> params = new HashMap<>();

    /**
     * Creates a new empty AssetURLBuilder.
     */
    public AssetURLBuilder() {
    }

    /**
     * Creates a new AssetURLBuilder from a URL string.
     * @param url the url string to parse.
     */
    public AssetURLBuilder(final String url) throws MalformedURLException {
        this(new AssetURL(url));
    }

    /**
     * Creates a new AssetURLBuilder from an existing AssetURL.
     * @param url the url to clone into the new builder.
     */
    public AssetURLBuilder(final AssetURL url) {
        mimeType = url.getMimeType();
        protocol = url.getProtocol();
        path = url.getPath();
        Iterator<Map.Entry<String, String>> it = url.parameters();
        if (it != null) {
            while (it.hasNext()) {
                Map.Entry<String, String> entry = it.next();
                params.put(entry.getKey(), entry.getValue());
            }
        }
    }

    //CHECKSTYLE OFF: HiddenField
    /**
     * Sets the mime-type.
     * @param mimeType the new mime-type.
     * @return this.
     */
    public AssetURLBuilder mimeType(final String mimeType) {
        this.mimeType = mimeType;
        return this;
    }

    /**
     * Sets the path.
     * @param path the new path.
     * @return this.
     */
    public AssetURLBuilder path(final String path) {
        this.path = path;
        return this;
    }

    /**
     * Sets the protocol.
     * @param protocol the new protocol.
     * @return this.
     */
    public AssetURLBuilder protocol(final String protocol) {
        this.protocol = protocol;
        return this;
    }
    //CHECKSTYLE ON

    /**
     * Appends a parameter.
     * @param name the parameter name.
     * @param value the parameter value.
     * @return this.
     */
    public AssetURLBuilder parameter(final String name, final String value) {
        if (name != null && value != null) {
            this.params.put(name, value);
        }
        return this;
    }

    /**
     * Constructs an AssetURL from this builder.
     * @return a new AssetURL.
     * @throws MalformedURLException if the created URL would be invalid.
     */
    public AssetURL build() throws MalformedURLException {
        if (protocol == null) {
            throw new MalformedURLException("Missing protocol when attempting to build AssetURL");
        }
        if (path == null) {
            throw new MalformedURLException("Missing path when attempting to build AssetURL");
        }
        return new AssetURL(mimeType, protocol, path, params);
    }
}
