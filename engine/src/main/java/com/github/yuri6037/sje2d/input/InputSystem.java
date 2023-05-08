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

package com.github.yuri6037.sje2d.input;

import com.github.yuri6037.sje2d.window.IInputHandler;

import java.util.HashMap;
import java.util.HashSet;

public final class InputSystem implements IInputHandler {
    private final HashSet<Key> keyStates = new HashSet<>();
    private final HashMap<Binding, Runnable> pressBindings = new HashMap<>();
    private final HashMap<Binding, Runnable> releaseBindings = new HashMap<>();
    private final IInputConfig config;

    public class BindBuilder {
        private final Binding binding;
        private boolean press = false;
        private boolean release = false;

        BindBuilder(final Binding binding) {
            this.binding = binding;
        }

        /**
         * Call the action function when the key is pressed.
         * @return self.
         */
        public BindBuilder onPress() {
            press = true;
            return this;
        }

        /**
         * Call the action function when the key is released.
         * @return self.
         */
        public BindBuilder onRelease() {
            release = true;
            return this;
        }

        /**
         * Binds the given action.
         * @param runnable the action function.
         * @return self.
         */
        public BindBuilder action(final Runnable runnable) {
            if (press) {
                pressBindings.put(binding, runnable);
            }
            if (release) {
                releaseBindings.put(binding, runnable);
            }
            return this;
        }
    }

    /**
     * Creates a new instance of an InputSystem.
     * @param config the input configuration (list of bindings, axes, etc).
     */
    public InputSystem(final IInputConfig config) {
        this.config = config;
    }

    /**
     * Binds an action to a binding.
     * @param binding the binding to bind an action to.
     * @return a builder to configure the bound action.
     */
    public BindBuilder bind(final Binding binding) {
        return new BindBuilder(binding);
    }

    /**
     * Gets a binding from its name.
     * @param name the name of the binding.
     * @return an instance of a binding or null if not found.
     */
    public Binding getBinding(final String name) {
        return config.getBinding(name);
    }

    /**
     * Gets an axis by its name.
     * @param name the name of the axis.
     * @return an instance of an axis or null if not found.
     */
    public Axis getAxis(final String name) {
        return config.getAxis(name);
    }

    /**
     * Checks if a key is currently being pressed.
     * @param key the key to check.
     * @return true if the key is being pressed, false otherwise.
     */
    public boolean isDown(final Key key) {
        return keyStates.contains(key);
    }

    /**
     * Checks if a binding is currently being pressed.
     * @param binding the binding to check.
     * @return true if the binding is being pressed, false otherwise.
     */
    public boolean isDown(final Binding binding) {
        return isDown(binding.getKey());
    }

    @Override
    public void onAxisUpdate(final String key, final float x, final float y, final float z) {
        for (Axis axis: config.getAxesForKey(key)) {
            axis.setX(x);
            axis.setY(y);
            axis.setZ(z);
        }
    }

    @Override
    public void onKeyPress(final Key key) {
        keyStates.add(key);
        for (Binding binding: config.getBindingsForKey(key)) {
            Runnable run = pressBindings.get(binding);
            if (run != null) {
                run.run();
            }
        }
    }

    @Override
    public void onKeyRelease(final Key key) {
        keyStates.remove(key);
        for (Binding binding: config.getBindingsForKey(key)) {
            Runnable run = releaseBindings.get(binding);
            if (run != null) {
                run.run();
            }
        }
    }

    @Override
    public void onTextInput(final String text) {

    }
}
