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

 package com.github.yuri6037.sje2d.examples.animations;

import com.github.yuri6037.sje2d.Application;
import com.github.yuri6037.sje2d.asset.Animation;
import com.github.yuri6037.sje2d.asset.engine.map.AssetStore;
import com.github.yuri6037.sje2d.render.Color;
import com.github.yuri6037.sje2d.screen.BasicScreen;

public final class Screen extends BasicScreen {
    private final AssetStore<Animation>.Ref animation;
    private final AssetStore<Animation>.Ref basic;

    /**
     * Creates a new BasicScreen.
     *
     * @param app the Application this screen is attached to.
     */
    public Screen(final Application app) {
        super(app);
        animation = getAssets().get(Animation.class, "Animation/loading");
        basic = getAssets().get(Animation.class, "Animation/basic");
    }

    @Override
    public void update() {
        getRender().setColor(Color.WHITE);

        getRender().setTexture(animation.get());
        getRender().setTextureRect(animation.get().getFrameRect(getTimer()));
        getRender().drawRect(width() / 2 - 128, height() / 2 - 128, 256, 256);

        getRender().setTexture(basic.get());
        getRender().setTextureRect(basic.get().getFrameRect(getTimer()));
        getRender().drawRect(0, 0, 256, 256);
    }
}
