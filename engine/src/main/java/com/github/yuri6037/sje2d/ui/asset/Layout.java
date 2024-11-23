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

package com.github.yuri6037.sje2d.ui.asset;

import com.github.yuri6037.sje2d.asset.engine.system.IAsset;
import com.github.yuri6037.sje2d.ui.component.Component;

import java.util.HashMap;
import java.util.Map;

public final class Layout implements IAsset {
    private final Component rootComponent;
    private final Map<String, Component> componentsById;

    /**
     * Creates a new UI Layout.
     * @param rootComponent the root component of this layout tree.
     * @param componentsById a map which assigns indices to some components in the tree.
     */
    public Layout(final Component rootComponent, final Map<String, Component> componentsById) {
        this.rootComponent = rootComponent;
        this.componentsById = componentsById;
    }

    /**
     * @return the root component of this layout tree.
     */
    public Component getRootComponent() {
        return rootComponent;
    }

    /**
     * Finds a component by its ID.
     * @param componentClass the class of the component.
     * @param id the index of the component to locate.
     * @return the matching component instance if found, otherwise null.
     * @param <T> the generic component type.
     */
    public <T extends Component> T getComponent(final Class<T> componentClass, final String id) {
        Component c = componentsById.get(id);
        if (c == null) {
            return null;
        }
        return componentClass.isInstance(c) ? componentClass.cast(c) : null;
    }

    /**
     * Creates a new instance of this Layout by duplicating all of its components and re-build the component map.
     * @return a new instance of this Layout tree.
     */
    public Layout instantiate() {
        try {
            Map<String, Component> comps = new HashMap<>();
            Component root = rootComponent.instantiate(comps);
            return new Layout(root, comps);
        } catch (CloneNotSupportedException e) {
            return null;
        }
    }

    @Override
    public void unload() {
    }
}
