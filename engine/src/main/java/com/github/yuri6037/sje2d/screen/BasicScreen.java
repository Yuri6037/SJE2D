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

package com.github.yuri6037.sje2d.screen;

import com.github.yuri6037.sje2d.Application;
import com.github.yuri6037.sje2d.asset.engine.manager.AssetManagerProxy;
import com.github.yuri6037.sje2d.render.Render;
import com.github.yuri6037.sje2d.util.Timer;

public abstract class BasicScreen implements IScreen {
    private final Render render = new Render();
    private final Application app;

    /**
     * Creates a new BasicScreen.
     * @param app the Application this screen is attached to.
     */
    public BasicScreen(final Application app) {
        this.app = app;
    }

    /**
     * @return the window width.
     */
    protected float width() {
        return app.getWindow().getWidth();
    }

    /**
     * @return the window height.
     */
    protected float height() {
        return app.getWindow().getHeight();
    }

    /**
     * @return the Application this screen is attached to.
     */
    protected Application getApp() {
        return app;
    }

    /**
     * @return an instance of the 2D rendering engine.
     */
    protected Render getRender() {
        return render;
    }

    /**
     * @return the application timer.
     */
    protected Timer getTimer() {
        return getApp().getTimer();
    }

    /**
     * @return the AssetManagerProxy.
     */
    protected AssetManagerProxy getAssets() {
        return getApp().getAssets();
    }

    @Override
    public void close() {
    }

    @Override
    public void open() {
    }
}
