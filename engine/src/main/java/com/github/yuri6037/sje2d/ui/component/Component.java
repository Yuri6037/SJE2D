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

package com.github.yuri6037.sje2d.ui.component;

import com.github.yuri6037.sje2d.reflect.Configurator;
import com.github.yuri6037.sje2d.reflect.IConfigFunction;
import com.github.yuri6037.sje2d.reflect.IConfigurable;
import com.github.yuri6037.sje2d.render.Color;
import com.github.yuri6037.sje2d.render.Point;
import com.github.yuri6037.sje2d.render.Size;
import com.github.yuri6037.sje2d.ui.core.input.IInput;
import com.github.yuri6037.sje2d.ui.core.render.IRender;
import com.github.yuri6037.sje2d.ui.core.render.Rect;

import java.util.Map;

/**
 * This is the base class for all types of components in the UI engine.
 */
public abstract class Component implements IComponent, IConfigurable, Cloneable {
    private Rect rect = new Rect();
    private boolean autoSize = false;
    private boolean showDebugBoundingBox = false;
    private final Configurator configurator = new Configurator();
    private String id;

    /**
     * Initialize a base Component.
     */
    public Component() {
        addParam("showDebugBoundingBox", Boolean.class, this::setShowDebugBoundingBox);
        addParam("autoSize", Boolean.class, this::setAutoSize);
        addParam("pos", Point.class, this::setPos);
        addParam("size", Size.class, this::setSize);
    }

    /**
     * Sets the ID of this component.
     * The ID acts as a string to find in a Layout tree asset.
     * @param id1 the new index of this component.
     * @return this for chaining operations.
     */
    public final Component setId(final String id1) {
        id = id1;
        return this;
    }

    /**
     * Sets whether this component should show its debug bounding box.
     * The debug bounding box shows as a yellow outline rectangle matching exactly the position and size of
     * the component.
     * @param showDebugBoundingBox1 true to draw the debug bounding box, false otherwise.
     * @return this for chaining operations.
     */
    public final Component setShowDebugBoundingBox(final boolean showDebugBoundingBox1) {
        showDebugBoundingBox = showDebugBoundingBox1;
        return this;
    }

    /**
     * Sets whether this component should automatically compute its size based on its content.
     * See Component::applyAutoSize for more information on how auto-sizing works.
     * @param autoSize1 true to compute the size automatically, false otherwise.
     * @return this for chaining operations.
     */
    public final Component setAutoSize(final boolean autoSize1) {
        autoSize = autoSize1;
        setSize(Size.ZERO);
        return this;
    }

    /**
     * Sets the position of this component.
     * @param pos the new position of this component.
     * @return this for chaining operations.
     */
    public final Component setPos(final Point pos) {
        rect.setPos(pos);
        return this;
    }

    /**
     * Sets the size of this component.
     * @param size the new size of this component.
     * @return this for chaining operations.
     */
    public final Component setSize(final Size size) {
        rect.setSize(size);
        return this;
    }

    //CHECKSTYLE OFF: FinalParameters
    /**
     * Applies an automatic size if this component should be auto sizing.
     * @param parentRect the parent rectangle if any.
     * @param width the absolute width in pixels.
     * @param height the absolute height in pixels.
     */
    protected final void applyAutoSize(final Rect parentRect, float width, float height) {
        if (shouldAutoSize()) {
            if (parentRect != null) {
                width = width / parentRect.getSize().width();
                height = height / parentRect.getSize().height();
            }
            setSize(new Size(width, height));
        }
    }
    //CHECKSTYLE ON

    /**
     * @return returns the rectangle which represents the bounding box of this component.
     */
    public final Rect getRect() {
        return rect;
    }

    /**
     * @return true if this component is automatically computing its size.
     */
    public final boolean shouldAutoSize() {
        return autoSize;
    }

    /**
     * @return the position of this component.
     */
    public final Point getPos() {
        return rect.getPos();
    }

    /**
     * @return the size of this component.
     */
    public final Size getSize() {
        return rect.getSize();
    }

    /**
     * Utility function to check if the mouse represented by the given UI Input engine is in the specified rectangle.
     * @param input the UI Input engine representing the mouse coordinates.
     * @param x the x position of the rectangle.
     * @param y the y position of the rectangle.
     * @param width the width of the rectangle.
     * @param height the height of the rectangle.
     * @return true if the mouse is inside the specified rectangle, false otherwise.
     */
    public static boolean isMouseIn(final IInput input, final float x, final float y, final float width,
                                    final float height) {
        return input.getMouse().getX() >= x && input.getMouse().getX() <= x + width
                && input.getMouse().getY() >= y && input.getMouse().getY() <= y + height;
    }

    /**
     * Utility function to check if the mouse represented by the given UI Input engine is in the specified rectangle.
     * @param input the UI Input engine representing the mouse coordinates.
     * @param pos the position of the rectangle.
     * @param size the size of the rectangle.
     * @return true if the mouse is inside the specified rectangle, false otherwise.
     */
    public static boolean isMouseIn(final IInput input, final Point pos, final Size size) {
        return input.getMouse().getX() >= pos.x() && input.getMouse().getX() <= pos.x() + size.width()
                && input.getMouse().getY() >= pos.y() && input.getMouse().getY() <= pos.y() + size.height();
    }

    /**
     * Utility function to check if the mouse represented by the given UI Input engine is in the specified rectangle.
     * @param input the UI Input engine representing the mouse coordinates.
     * @param rect the target rectangle.
     * @return true if the mouse is inside the specified rectangle, false otherwise.
     */
    public static boolean isMouseIn(final IInput input, final Rect rect) {
        return isMouseIn(input, rect.getPos(), rect.getSize());
    }

    private void applyDebugBoundingBox(final IRender render, final Rect rect1) {
        if (showDebugBoundingBox) {
            render.setColor(Color.YELLOW);
            render.setTexture(null);
            render.drawOutlineRect(rect1.getPos().x(), rect1.getPos().y(), rect1.getSize().width(),
                    rect1.getSize().height(), 4f);
        }
    }

    //CHECKSTYLE OFF: HiddenField
    /**
     * Updates and renders this component. This default method implementation provides the necessary support to render
     * the debug bounding box. You should always call this method in derived classes if you want the debug bounding box
     * function.
     * @param parentRect the parent rectangle in pixels if this object is proportional relative to its parent.
     *                   This is used for auto-sizing.
     * @param render an instance of the UI rendering engine.
     * @param input an instance of the UI input engine.
     * @param rect the rectangle in pixels in which this component should be rendered.
     */
    @Override
    public void update(final Rect parentRect, final IRender render, final IInput input, final Rect rect) {
        applyDebugBoundingBox(render, rect);
    }
    //CHECKSTYLE ON

    @Override
    public final void setParam(final String key, final Object value) throws IllegalArgumentException {
        configurator.setParam(key, value);
    }

    @Override
    public final Class<?> getParamType(final String key) {
        return configurator.getParamType(key);
    }

    /**
     * Adds a configuration parameter to this component class.
     * @param key the name of the parameter to be configured.
     * @param parameterClass the type class of the parameter value.
     * @param function the set function to bind to the parameter.
     * @param <V> the generic type of the parameter value.
     */
    protected final <V> void addParam(final String key, final Class<V> parameterClass,
                                      final IConfigFunction<V> function) {
        configurator.addParam(key, parameterClass, function);
    }

    /**
     * Creates a new instance of this component.
     * This essentially clones this component tree and fills the map with component associations.
     * @param componentsById the map of all components with an ID assigned.
     * @return the instance of the newly cloned component.
     */
    public Component instantiate(final Map<String, Component> componentsById) throws CloneNotSupportedException {
        Component obj = (Component) super.clone();
        if (id != null) {
            componentsById.put(id, obj);
        }
        obj.rect = new Rect();
        obj.rect.setPos(rect.getPos());
        obj.rect.setSize(rect.getSize());
        return obj;
    }
}
