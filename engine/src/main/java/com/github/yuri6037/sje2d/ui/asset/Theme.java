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
import com.github.yuri6037.sje2d.reflect.Configurator;
import com.github.yuri6037.sje2d.reflect.IConfigurable;
import com.github.yuri6037.sje2d.ui.asset.style.RectangleStyle;
import com.github.yuri6037.sje2d.ui.asset.style.Style;
import com.github.yuri6037.sje2d.ui.asset.style.TextStyle;

import java.util.HashMap;

/**
 * A theme asset is just a default style provider for each type of component.
 */
public final class Theme implements IAsset, IConfigurable {
    /**
     * A theme style key.
     */
    public static class Key {
        private final String name;
        private final Class<? extends Style> styleClass;

        /**
         * Creates a new theme style index key.
         * WARNING: This should be instanced as a static.
         * @param name the name of the key.
         * @param styleClass the style class type.
         */
        public Key(final String name, final Class<? extends Style> styleClass) {
            this.name = name;
            this.styleClass = styleClass;
            Theme.KEY_REGISTRY.put(name, this);
        }

        /**
         * @return the key name.
         */
        public String getName() {
            return name;
        }
    }

    private static final HashMap<String, Key> KEY_REGISTRY = new HashMap<>();

    private RectangleStyle rectangleStyle;
    private TextStyle textStyle;
    private final HashMap<Key, Style> styleByKey = new HashMap<>();

    private final Configurator configurator = new Configurator();

    /**
     * Creates a new Theme asset.
     */
    public Theme() {
    }

    /**
     * Sets the default style to apply to a rectangle primitive.
     * @param style the new style.
     */
    public void setRectangleStyle(final RectangleStyle style) {
        rectangleStyle = style;
    }

    /**
     * Sets the default style to apply to a text / 3D text primitive.
     * @param style the new style.
     */
    public void setTextStyle(final TextStyle style) {
        textStyle = style;
    }

    /**
     * @return the default style to apply to a rectangle primitive.
     */
    public RectangleStyle getRectangleStyle() {
        return rectangleStyle;
    }

    /**
     * @return the default style to apply to a text / 3D text primitive.
     */
    public TextStyle getTextStyle() {
        return textStyle;
    }

    /**
     * Puts a style into this theme.
     * @param themeKey the theme style key.
     * @param style the style to put.
     * @throws IllegalArgumentException when the given style does not match the class registered for this theme key.
     */
    public void putStyle(final Key themeKey, final Style style) throws IllegalArgumentException {
        if (!themeKey.styleClass.equals(style.getClass())) {
            throw new IllegalArgumentException("Invalid class '" + style.getClass().getName() + "' for theme key '"
                    + themeKey.getName() + "'");
        }
        styleByKey.put(themeKey, style);
    }

    /**
     * Puts a style into this theme.
     * This function searches for a theme key with the given name to attach the style to.
     * @param keyName the theme key name.
     * @param style the style to put.
     * @throws IllegalArgumentException when the given style does not match the class registered for this theme key.
     */
    public void putStyle(final String keyName, final Style style) throws IllegalArgumentException {
        Key themeKey = KEY_REGISTRY.get(keyName);
        if (themeKey == null) {
            throw new IllegalArgumentException("Theme key with name '" + keyName + "' does not exist");
        }
        putStyle(themeKey, style);
    }

    /**
     * Finds the style to apply for the given theme key.
     * @param styleClass the style class to cast to.
     * @param themeKey the theme key to look for.
     * @return an instance of the style attached to the given theme key.
     * @param <T> the generic style type to cast to.
     */
    public <T extends Style> T getStyle(final Class<T> styleClass, final Key themeKey) {
        return styleClass.cast(styleByKey.get(themeKey));
    }

    @Override
    public void unload() {
    }

    @Override
    public void setParam(final String key, final Object value) throws IllegalArgumentException {
        configurator.setParam(key, value);
    }

    @Override
    public Class<?> getParamType(final String key) {
        return configurator.getParamType(key);
    }
}
