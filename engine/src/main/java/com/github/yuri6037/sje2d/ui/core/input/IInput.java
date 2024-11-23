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

/**
 * The UI Input Engine entry point.
 */
public interface IInput {
    /**
     * @return the standard mouse X-Y axis.
     */
    Axis getMouse();

    /**
     * @return the standard mouse wheel X-Y axis.
     */
    Axis getMouseWheel();

    /**
     * Checks whether a key was pressed.
     * @param key the key to check.
     * @return true if the key was pressed, false otherwise.
     */
    boolean wasKeyDown(Key key);

    /**
     * Checks whether a key is currently being pressed.
     * @param key the key to check.
     * @return true if the key is currently being pressed, false otherwise.
     */
    boolean isKeyDown(Key key);

    /**
     * @return the last text input event.
     */
    String getLastTextEvent();

    /**
     * Clears all events of the current frame.
     * This includes text events, key press status and axis values.
     */
    void clear();
}
