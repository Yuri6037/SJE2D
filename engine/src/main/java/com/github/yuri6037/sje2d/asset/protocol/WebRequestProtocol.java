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

package com.github.yuri6037.sje2d.asset.protocol;

import com.github.yuri6037.sje2d.asset.engine.AssetURL;
import com.github.yuri6037.sje2d.asset.engine.system.IAssetProtocol;
import com.github.yuri6037.sje2d.asset.engine.system.stream.IAssetStream;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Map;

public abstract class WebRequestProtocol implements IAssetProtocol {
    private static final Map<String, String> CONTENT_TYPE_MAP = Map.of(
            "image/png", "texture/png",
            "image/jpeg", "texture/jpeg",
            "image/jpg", "texture/jpeg",
            "text/x-lua", "script/lua",
            "text/x-glsl", "shader/glsl"
    );

    @Override
    public final boolean canProvideMimeType() {
        return true;
    }

    private String attemptInferMimeType(final String contentType) {
        if (contentType == null) {
            return null;
        }
        String mimeType = CONTENT_TYPE_MAP.get(contentType);
        return mimeType != null ? mimeType : contentType;
    }

    @Override
    public final IAssetStream open(final AssetURL url) throws Exception {
        URL webUrl = new URL(url.withMimeType(null).toString());
        HttpURLConnection request = (HttpURLConnection) webUrl.openConnection();
        request.setRequestMethod("GET");
        request.setInstanceFollowRedirects(false);
        //Store the asset mime-type in X-Asset-Type if it exists otherwise guess it from Content-Type.
        String mimeType = request.getHeaderField("X-Asset-Type");
        if (mimeType == null) {
            mimeType = attemptInferMimeType(request.getHeaderField("Content-Type"));
        }
        InputStream is = request.getInputStream();
        return new BasicAssetStream(mimeType, is);
    }
}
