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

package com.github.yuri6037.sje2d.ui.panel;

import com.github.yuri6037.sje2d.ui.asset.Theme;
import com.github.yuri6037.sje2d.ui.component.Component;
import com.github.yuri6037.sje2d.ui.core.input.IInput;
import com.github.yuri6037.sje2d.ui.core.render.IRender;

import java.util.ArrayList;
import java.util.Map;

/**
 * A base class for all panel types.
 */
public abstract class BasePanel extends Component implements IPanel {
    private ArrayList<Component> components = new ArrayList<>();
    private boolean proportional = false;

    /**
     * Creates a new instance of a BasePanel.
     */
    public BasePanel() {
        addParam("proportional", Boolean.class, this::setProportional);
    }

    /**
     * @return the list of live components to be updated/rendered stored in this BasePanel.
     */
    protected final ArrayList<Component> getComponents() {
        return components;
    }

    /**
     * Sets whether this panel uses proportional positioning.
     * @param proportional1 true to use proportional positioning, false otherwise.
     * @return this for chaining operations.
     */
    public final BasePanel setProportional(final boolean proportional1) {
        this.proportional = proportional1;
        return this;
    }

    /**
     * @return true if this panel uses proportional positioning, false otherwise.
     */
    public boolean isProportional() {
        return proportional;
    }

    @Override
    public final BasePanel add(final Component component) {
        components.add(component);
        return this;
    }

    @Override
    public final BasePanel remove(final Component component) {
        components.remove(component);
        return this;
    }

    /**
     * Updates and renders this panel with no parent rect.
     * @param render an instance of the UI rendering engine.
     * @param input an instance of the UI input engine.
     */
    public final void update(final IRender render, final IInput input) {
        update(null, render, input, getRect());
    }

    /**
     * Creates a new instance of this component.
     * This essentially clones this component tree and fills the map with component associations.
     * @param componentsById the map of all components with an ID assigned.
     */
    @Override
    public Component instantiate(final Map<String, Component> componentsById) throws CloneNotSupportedException {
        BasePanel comp = (BasePanel) super.instantiate(componentsById);
        comp.components = new ArrayList<>();
        for (Component c: components) {
            comp.components.add(c.instantiate(componentsById));
        }
        return comp;
    }

    @Override
    public void applyDefaultStyle(final Theme theme) {
    }
}
