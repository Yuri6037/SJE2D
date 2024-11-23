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

package com.github.yuri6037.sje2d.ui.core.input;

import com.github.yuri6037.sje2d.input.Axis;
import com.github.yuri6037.sje2d.input.Key;
import com.github.yuri6037.sje2d.window.AxisKey;
import com.github.yuri6037.sje2d.window.IInputHandler;

import java.util.ArrayDeque;
import java.util.HashSet;
import java.util.Queue;

public final class SimpleInput implements IInput, IInputHandler {
    private final Axis mouse = new Axis("mouse");
    private final Axis wheel = new Axis("wheel");
    private final HashSet<Key> keyDown = new HashSet<>();
    private final HashSet<Key> wasKeyDown = new HashSet<>();
    private final Queue<String> textEvents = new ArrayDeque<>();

    @Override
    public Axis getMouse() {
        return mouse;
    }

    @Override
    public Axis getMouseWheel() {
        return wheel;
    }

    @Override
    public boolean wasKeyDown(final Key key) {
        return wasKeyDown.remove(key);
    }

    @Override
    public void clear() {
        wasKeyDown.clear();
        textEvents.clear();
        wheel.setX(0);
        wheel.setY(0);
    }

    @Override
    public boolean isKeyDown(final Key key) {
        return keyDown.contains(key);
    }

    @Override
    public String getLastTextEvent() {
        return textEvents.poll();
    }

    @Override
    public void onAxisUpdate(final String key, final float x, final float y, final float z) {
        if (AxisKey.MOUSE.equals(key)) {
            mouse.setX(x);
            mouse.setY(y);
        } else if (AxisKey.MOUSE_WHEEL.equals(key)) {
            wheel.setX(x);
            wheel.setY(y);
        }
    }

    @Override
    public void onKeyPress(final Key key) {
        keyDown.add(key);
    }

    @Override
    public void onKeyRelease(final Key key) {
        keyDown.remove(key);
        wasKeyDown.add(key);
    }

    @Override
    public void onTextInput(final String text) {
        textEvents.add(text);
    }
}
