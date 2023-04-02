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

package com.github.yuri6037.sje2d.asset.engine;

import com.github.yuri6037.sje2d.util.StringUtils;

import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public final class AssetURL {
    private final String mimeType;
    private final String protocol;
    private final String path;
    private final HashMap<String, String> queryParams;

    /**
     * Constructs a new AssetURL by parsing an url string.
     * The url string has the following form:
     * ([mime-type] WS) [protocol]://[path](?[query-param])*
     * @param url the url string to parse
     * @throws MalformedURLException if the URL string does not have the correct format.
     */
    public AssetURL(final String url) throws MalformedURLException {
        int id = url.indexOf("://");
        if (id == -1) {
            throw new MalformedURLException("No protocol specified");
        }
        String protoMimeType = url.substring(0, id);
        String pathQuery = url.substring(id + 3);
        id = protoMimeType.indexOf(' ');
        if (id == -1) {
            mimeType = null;
            protocol = protoMimeType;
        } else {
            mimeType = protoMimeType.substring(0, id);
            protocol = protoMimeType.substring(id + 1);
        }
        if (pathQuery.isEmpty() || pathQuery.isBlank()) {
            throw new MalformedURLException("No path specified");
        }
        id = pathQuery.indexOf('?');
        if (id == -1) {
            path = pathQuery;
            queryParams = null;
        } else {
            path = pathQuery.substring(0, id);
            String query = pathQuery.substring(id + 1);
            queryParams = new HashMap<>();
            for (String s : query.split("&")) {
                id = s.indexOf('=');
                if (id == -1) {
                    throw new MalformedURLException("Bad parameter string '" + s + "'");
                }
                queryParams.put(s.substring(0, id), s.substring(id + 1));
            }
        }
    }

    /**
     * Constructs a new AssetURL using a protocol and a path.
     * NOTE: Using this constructor the mime-type is not specified. To specify a mime type see
     * AssetURL(String, String, String).
     * @param protocol the URL protocol.
     * @param path the URL path with 0 or more query parameters.
     * @throws MalformedURLException if the path contains query parameters with a wrong format.
     */
    public AssetURL(final String protocol, final String path) throws MalformedURLException {
        this(null, protocol, path);
    }

    /**
     * Constructs a new AssetURL with a specific mime-type, a protocol and a path.
     * @param mimeType the com.github.yuri6037.minengine.test.asset mime-type.
     * @param protocol the URL protocol.
     * @param path the URL path with 0 or more query parameters.
     * @throws MalformedURLException if the path contains query parameters with a wrong format.
     */
    public AssetURL(final String mimeType, final String protocol, final String path) throws MalformedURLException {
        if (protocol == null) {
            throw new MalformedURLException("No protocol specified");
        }
        if (path == null) {
            throw new MalformedURLException("No path specified");
        }
        this.mimeType = mimeType;
        this.protocol = protocol;
        int id = path.indexOf('?');
        if (id == -1) {
            this.path = path;
            queryParams = null;
        } else {
            this.path = path.substring(0, id);
            String query = path.substring(id + 1);
            queryParams = new HashMap<>();
            for (String s : query.split("&")) {
                id = s.indexOf('=');
                if (id == -1) {
                    throw new MalformedURLException("Bad parameter string '" + s + "'");
                }
                queryParams.put(s.substring(0, id), s.substring(id + 1));
            }
        }
    }

    private AssetURL(final String mimeType, final String protocol, final String path,
                     final HashMap<String, String> queryParams) {
        this.mimeType = mimeType;
        this.protocol = protocol;
        this.path = path;
        this.queryParams = queryParams;
    }

    /**
     * @return the mime type or null if none.
     */
    public String getMimeType() {
        return mimeType;
    }

    /**
     * @return the protocol name.
     */
    public String getProtocol() {
        return protocol;
    }

    /**
     * @return the URL path (without query parameters).
     */
    public String getPath() {
        return path;
    }

    /**
     * @return true if this AssetURL points to a file.
     */
    public boolean isFile() {
        return protocol.equals("file");
    }

    /**
     * Gets a query parameter by name.
     * @param name the name of the query parameter.
     * @return the query parameter value if the name was found, null otherwise.
     */
    public String getParameter(final String name) {
        return queryParams == null ? null : queryParams.get(name);
    }

    //CHECKSTYLE OFF: HiddenField
    /**
     * Replaces the mime-type of this URL with a different one.
     * @param mimeType the new mime-type of this URL.
     * @return a copy of this asset URL with the new mime-type.
     */
    public AssetURL withMimeType(final String mimeType) {
        return new AssetURL(mimeType, protocol, path, queryParams);
    }
    //CHECKSTYLE ON

    /**
     * Guesses the type of the loaded asset from the mime-type specified in this url.
     * @throws MalformedURLException if this asset does not have a mime-type.
     * @return guessed asset type string.
     */
    public String guessAssetType() throws MalformedURLException {
        if (mimeType == null) {
            throw new MalformedURLException("No mime-type specified");
        }
        int id = mimeType.indexOf('/');
        if (id != -1) {
            return StringUtils.capitalize(mimeType.substring(0, id));
        } else {
            return StringUtils.capitalize(mimeType);
        }
    }

    /**
     * Guesses the virtual path of this asset URL.
     * WARNING: There is no guarantee that the returned virtual path will be correct as the asset system can always
     * override the virtual path.
     * @return the virtual path which should be used to reference this asset.
     * @throws MalformedURLException if this asset does not have a mime-type and no vpath query parameter was specified.
     */
    public String guessVirtualPath() throws MalformedURLException {
        String namespace = getParameter("namespace");
        String vpath = getParameter("vpath");
        //If we have an explicit vpath specified, return it.
        if (vpath != null) {
            return namespace != null ? namespace + "/" + vpath : vpath;
        }
        //If no explicit vpath was specified, try to guess one.
        String assetType = guessAssetType();
        String[] split = getPath().split("/");
        //Compute the inverse list of path components to compose the guessed virtual path.
        ArrayList<String> components = new ArrayList<>();
        String name = split[split.length - 1];
        int id = name.indexOf('.');
        if (id != -1) {
            components.add(name.substring(0, id));
        } else {
            components.add(name);
        }
        for (int i = split.length - 2; i > 0; --i) {
            if (!assetType.equalsIgnoreCase(split[i])) {
                components.add(split[i]);
            } else {
                break;
            }
        }
        components.add(assetType);
        if (namespace != null) {
            components.add(namespace);
        }
        //Reverse the components list and join it with '/'
        Collections.reverse(components);
        return String.join("/", components);
    }

    @Override
    public String toString() {
        StringBuilder url = new StringBuilder();
        if (mimeType != null) {
            url.append(mimeType).append(' ');
        }
        url.append(protocol).append("://").append(path);
        if (queryParams != null) {
            boolean first = true;
            for (Map.Entry<String, String> entry: queryParams.entrySet()) {
                if (first) {
                    url.append('?');
                    first = false;
                } else {
                    url.append('&');
                }
                url.append(entry.getKey()).append('=').append(entry.getValue());
            }
        }
        return url.toString();
    }
}
