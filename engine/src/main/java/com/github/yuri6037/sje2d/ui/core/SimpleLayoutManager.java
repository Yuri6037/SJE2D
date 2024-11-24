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

package com.github.yuri6037.sje2d.ui.core;

import com.github.yuri6037.sje2d.Application;
import com.github.yuri6037.sje2d.render.Point;
import com.github.yuri6037.sje2d.render.Render;
import com.github.yuri6037.sje2d.render.Size;
import com.github.yuri6037.sje2d.ui.asset.Layout;
import com.github.yuri6037.sje2d.ui.core.input.IInput;
import com.github.yuri6037.sje2d.ui.core.input.SimpleInput;
import com.github.yuri6037.sje2d.ui.core.render.IRender;
import com.github.yuri6037.sje2d.ui.core.render.SimpleRender;
import com.github.yuri6037.sje2d.ui.panel.BasePanel;
import com.github.yuri6037.sje2d.ui.panel.Panel;
import com.github.yuri6037.sje2d.window.IInputHandler;

/**
 * A simple LayoutManager which maps to the base Render, FontRender and the base input system of SJE2D.
 */
public final class SimpleLayoutManager implements ILayoutManager {
    private final SimpleInput input = new SimpleInput();
    private final SimpleRender render;
    private final Application app;

    private final BasePanel contentPanel;

    /**
     * Creates a new instance of SimpleLayoutManager.
     * @param app an instance of the main application.
     * @param render an instance of the base SJE2D Render.
     */
    public SimpleLayoutManager(final Application app, final Render render) {
        this.render = new SimpleRender(app.getAssets(), app.getWindow(), render);
        this.app = app;
        contentPanel = (BasePanel) new Panel().setProportional(true).setPos(Point.ZERO);
    }

    /**
     * Creates a new instance of SimpleLayoutManager.
     * @param app an instance of the main application (the base SJE2D Render will be extracted from this app instance).
     */
    public SimpleLayoutManager(final Application app) {
        render = new SimpleRender(app.getAssets(), app.getWindow(), new Render());
        this.app = app;
        contentPanel = (BasePanel) new Panel().setProportional(true).setPos(Point.ZERO);
    }

    /**
     * Adds the given layout to the content panel.
     * @param layout the layout to add to the content panel.
     */
    public void addLayout(final Layout layout) {
        contentPanel.add(layout.getRootComponent());
    }

    /**
     * Removes the given layout from the content panel.
     * @param layout the layout to remove from the content panel.
     */
    public void removeLayout(final Layout layout) {
        contentPanel.remove(layout.getRootComponent());
    }

    /**
     * Main loop entry point. This updates and renders all registered surfaces.
     * @param width the width of the rendering target.
     * @param height the height of the rendering target.
     */
    public void update(final float width, final float height) {
        contentPanel.setSize(new Size(width, height));
        contentPanel.update(render, input);
        input.clear();
    }

    /**
     * @return the IInputHandler to be attached to the window in order for this LayoutManager to correctly handle
     * input events.
     * It is the responsibility of the caller to ensure the IInputHandler is correctly restored.
     */
    public IInputHandler getInputHandler() {
        return input;
    }

    /**
     * @return an instance of the UI Render Engine used by this LayoutManager.
     */
    @Override
    public IRender getRender() {
        return render;
    }

    /**
     * @return an instance of the UI Input Engine used by this LayoutManager.
     */
    @Override
    public IInput getInput() {
        return input;
    }
}
