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
import com.github.yuri6037.sje2d.asset.engine.system.IAsset;
import com.github.yuri6037.sje2d.asset.engine.system.stream.IAssetStream;
import com.github.yuri6037.sje2d.asset.engine.system.stream.StreamUtils;
import com.github.yuri6037.sje2d.asset.factory.base.AsyncLoader;
import com.github.yuri6037.sje2d.config.StyleType;
import com.github.yuri6037.sje2d.config.ThemeType;
import com.github.yuri6037.sje2d.ui.asset.Theme;
import com.github.yuri6037.sje2d.ui.asset.style.RectangleStyle;
import com.github.yuri6037.sje2d.ui.asset.style.Style;
import com.github.yuri6037.sje2d.ui.asset.style.TextStyle;

import javax.xml.bind.JAXBContext;
import javax.xml.transform.stream.StreamSource;
import java.io.InputStream;

public final class ThemeLoader extends AsyncLoader<Theme> {
    private Theme theme;
    private final InputStream stream;

    /**
     * Creates a new Theme XML loader.
     * @param url the url of the asset to load.
     * @param stream the stream to read from.
     */
    public ThemeLoader(final AssetURL url, final IAssetStream stream) {
        super(url);
        this.stream = StreamUtils.makeInputStream(stream);
    }

    @Override
    public void loadAsync() throws Exception {
        JAXBContext ctx = JAXBContext.newInstance(ThemeType.class);
        ThemeType xml = ctx.createUnmarshaller().unmarshal(new StreamSource(stream), ThemeType.class).getValue();
        theme = new Theme();
        theme.setRectangleStyle(awaitAsset(RectangleStyle.class, xml.getRectangle()));
        theme.setTextStyle(awaitAsset(TextStyle.class, xml.getText()));
        if (xml.getStyle() != null) {
            for (StyleType ty: xml.getStyle()) {
                String keyName = ty.getKey();
                String vpath = ty.getValue();
                IAsset asset = awaitAsset(vpath);
                theme.putStyle(keyName, (Style) asset);
            }
        }
    }

    @Override
    protected Theme createAsset() {
        return theme;
    }
}
