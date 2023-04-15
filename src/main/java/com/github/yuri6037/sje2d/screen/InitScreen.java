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

package com.github.yuri6037.sje2d.screen;

import com.github.yuri6037.sje2d.Application;
import com.github.yuri6037.sje2d.asset.Texture;
import com.github.yuri6037.sje2d.asset.engine.map.AssetStore;
import com.github.yuri6037.sje2d.render.Color;
import com.github.yuri6037.sje2d.render.Point;

public final class InitScreen extends BasicScreen {
    private static final float SCALE_SPEED = 0.5f;
    private static final float SCALE_LOW = 0.5f;
    private static final float SCALE_HIGH = 1.5f;

    private final AssetStore<Texture>.Ref texture;
    private float scale = 1.0f;
    private float scaleF = 1.0f;

    /**
     * Creates a new BasicScreen.
     * @param app the Application this screen is attached to.
     */
    public InitScreen(final Application app) {
        super(app);
        texture = getApp().getAssets().get(Texture.class, "Engine/Texture/Init");
    }

    @Override
    public void open() {
        getRender().setTransformCenter(new Point(0.5f, 0.5f));
        getRender().setColor(Color.WHITE);
    }

    @Override
    public void update(final float deltaTime) {
        scale += scaleF * deltaTime;
        if (scale >= SCALE_HIGH) {
            scaleF = -SCALE_SPEED;
        } else if (scale <= SCALE_LOW) {
            scaleF = SCALE_SPEED;
        }
        getRender().setTexture(texture);
        getRender().setScale(scale);
        getRender().drawRect(width() / 2 - 256, height() / 2 - 256, 512, 512);
    }
}
