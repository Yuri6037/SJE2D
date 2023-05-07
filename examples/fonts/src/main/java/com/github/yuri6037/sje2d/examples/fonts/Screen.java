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

package com.github.yuri6037.sje2d.examples.fonts;

import com.github.yuri6037.sje2d.Application;
import com.github.yuri6037.sje2d.asset.Font;
import com.github.yuri6037.sje2d.render.Color;
import com.github.yuri6037.sje2d.render.FontRender;
import com.github.yuri6037.sje2d.render.Size;
import com.github.yuri6037.sje2d.screen.BasicScreen;
import com.github.yuri6037.sje2d.util.UTF32Str;

import java.io.UnsupportedEncodingException;

public final class Screen extends BasicScreen {
    private final FontRender fontRender;
    private final UTF32Str sampleText;

    /**
     * Creates a new BasicScreen.
     *
     * @param app the Application this screen is attached to.
     */
    public Screen(final Application app) {
        super(app);
        fontRender = new FontRender(getAssets().get(Font.class, "Engine/Font/Default"));
        try {
            sampleText = new UTF32Str("你好，Hello, 😀");
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void update() {
        fontRender.setShadowColor(new Color(255, 255, 255, 128));
        fontRender.setTextColor(Color.WHITE);
        fontRender.draw3DString(getAssets(), sampleText, 0, 0);
        fontRender.setTextColor(Color.RED);
        Size size = fontRender.getStringSize(getAssets(), sampleText);
        fontRender.drawString(getAssets(), sampleText, width() - size.width(), height() - size.height());
        fontRender.setRotation(45);
        fontRender.setShadowColor(new Color(255, 0, 0, 128));
        fontRender.draw3DString(getAssets(), sampleText, width() / 2 - size.width() / 2,
                height() / 2 - size.height() / 2);
        fontRender.setRotation(0.0f);
    }
}
