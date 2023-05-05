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

package com.github.yuri6037.sje2d;

import com.github.yuri6037.sje2d.asset.config.IAssetConfig;
import com.github.yuri6037.sje2d.asset.config.TypeRegistry;
import com.github.yuri6037.sje2d.asset.engine.system.IAssetFactory;
import com.github.yuri6037.sje2d.asset.engine.system.IAssetProtocol;
import com.github.yuri6037.sje2d.config.AppType;
import com.github.yuri6037.sje2d.config.AxisType;
import com.github.yuri6037.sje2d.config.BindingType;
import com.github.yuri6037.sje2d.config.DisplayType;
import com.github.yuri6037.sje2d.input.Axis;
import com.github.yuri6037.sje2d.input.Binding;
import com.github.yuri6037.sje2d.input.IInputConfig;
import com.github.yuri6037.sje2d.input.Key;
import com.github.yuri6037.sje2d.window.IWindowConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.xml.bind.JAXBElement;
import javax.xml.namespace.QName;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

final class XMLAppConfig implements IInputConfig, IWindowConfig, IAssetConfig {
    private static final Logger LOGGER = LoggerFactory.getLogger(XMLAppConfig.class);

    private static final QName NAME_PROTOCOL = new QName("sje2d", "protocol");
    private static final QName NAME_FACTORY = new QName("sje2d", "factory");

    private final HashMap<String, Binding> bindings = new HashMap<>();
    private final HashMap<Key, List<Binding>> bindingsInv = new HashMap<>();
    private final HashMap<String, Axis> axes = new HashMap<>();
    private final HashMap<String, List<Axis>> axesInv = new HashMap<>();
    private final boolean useJoystick;
    private final boolean useMouse;
    private final boolean useKeyboard;
    private final DisplayType displayConfig;
    private final ArrayList<IAssetProtocol> assetProtocols = new ArrayList<>();
    private final ArrayList<IAssetFactory> assetFactories = new ArrayList<>();

    XMLAppConfig(final AppType appcfg) {
        for (BindingType binding: appcfg.getInput().getBindings().getBinding()) {
            Key key = Key.fromString(binding.getKey());
            if (key == null) {
                LOGGER.warn("Unknown key '{}', binding '{}' has been set to the ESCAPE key", binding.getKey(),
                        binding.getId());
                key = Key.ESCAPE;
            }
            if (bindings.containsKey(binding.getId())) {
                LOGGER.warn("Duplicate binding ID '{}', ignoring...", binding.getId());
                continue;
            }
            Binding b = new Binding(binding.getId(), key);
            bindings.put(binding.getId(), b);
            if (!bindingsInv.containsKey(key)) {
                bindingsInv.put(key, new ArrayList<>());
            }
            bindingsInv.get(key).add(b);
        }
        for (AxisType axis: appcfg.getInput().getAxes().getAxis()) {
            if (axes.containsKey(axis.getId())) {
                LOGGER.warn("Duplicate axis ID '{}', ignoring...", axis.getId());
                continue;
            }
            Axis a = new Axis(axis.getId());
            axes.put(axis.getId(), a);
            if (!axesInv.containsKey(axis.getKey())) {
                axesInv.put(axis.getKey(), new ArrayList<>());
            }
            axesInv.get(axis.getKey()).add(a);
        }
        useJoystick = appcfg.getInput().isUseJoystick();
        useKeyboard = appcfg.getInput().isUseKeyboard();
        useMouse = appcfg.getInput().isUseMouse();
        displayConfig = appcfg.getDisplay();
        if (displayConfig.getTitle() == null) {
            displayConfig.setTitle(appcfg.getName() + " - " + appcfg.getVersion());
        }
        for (JAXBElement<String> elem: appcfg.getAssets().getRegistry().getProtocolOrFactory()) {
            try {
                Class<?> cl = Class.forName(elem.getValue());
                Object obj = cl.getDeclaredConstructor().newInstance();
                if (elem.getName().equals(NAME_PROTOCOL)) {
                    IAssetProtocol proto = (IAssetProtocol) obj;
                    assetProtocols.add(proto);
                } else if (elem.getName().equals(NAME_FACTORY)) {
                    IAssetFactory factory = (IAssetFactory) obj;
                    assetFactories.add(factory);
                }
            } catch (ClassNotFoundException | InvocationTargetException | InstantiationException
                     | IllegalAccessException | NoSuchMethodException | ClassCastException e) {
                LOGGER.warn("Failed to add type '{}' to registry, ignoring...", elem.getValue(), e);
            }
        }
    }

    @Override
    public Binding getBinding(final String name) {
        return bindings.get(name);
    }

    @Override
    public List<Binding> getBindingsForKey(final Key key) {
        return bindingsInv.get(key);
    }

    @Override
    public List<Axis> getAxesForKey(final String key) {
        return axesInv.get(key);
    }

    @Override
    public Axis getAxis(final String name) {
        return axes.get(name);
    }

    @Override
    public boolean supportsJoystick() {
        return useJoystick;
    }

    @Override
    public boolean supportsMouse() {
        return useMouse;
    }

    @Override
    public boolean supportsKeyboard() {
        return useKeyboard;
    }

    @Override
    public int getWidth() {
        return displayConfig.getWidth();
    }

    @Override
    public int getHeight() {
        return displayConfig.getHeight();
    }

    @Override
    public String getTitle() {
        return displayConfig.getTitle();
    }

    @Override
    public boolean isFullscreen() {
        return displayConfig.isFullscreen();
    }

    @Override
    public boolean isVsync() {
        return displayConfig.isVsync();
    }

    @Override
    public void populateTypeRegistry(final TypeRegistry.Builder registry) {
        for (IAssetFactory factory: assetFactories) {
            registry.addFactory(factory);
        }
        for (IAssetProtocol protocol: assetProtocols) {
            registry.addProtocol(protocol);
        }
    }
}
