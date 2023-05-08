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

package com.github.yuri6037.sje2d.util;

import static org.lwjgl.glfw.GLFW.glfwGetTime;

public class Timer {
    private float deltaTime;
    private double time;
    private float incTime = 0.0f;
    private float speed = 1.0f;
    private int frames = 0;
    private int lastFrames = 0;

    /**
     * Creates a new timer.
     */
    public Timer() {
        time = glfwGetTime();
    }

    /**
     * @return the delta time in seconds between the last frame in the current frame multiplied by the
     * speed of the timer.
     */
    public float getDeltaTime() {
        return deltaTime;
    }

    /**
     * @return the time in seconds since the start of the application.
     */
    public double getTime() {
        return time;
    }

    /**
     * @return the speed multiplier of the timer.
     */
    public float getSpeed() {
        return speed;
    }

    /**
     * Sets the speed multiplier of the timer.
     * @param speed the new speed multiplier.
     */
    public void setSpeed(final float speed) {
        this.speed = speed;
    }

    /**
     * @return the number of frames per second.
     */
    public int getFPS() {
        return lastFrames;
    }

    /**
     * Updates this timer.
     * Call this function at the beginning of each frame.
     * WARNING: Do not call this method if you're not the direct owner of the timer,
     * unless you know what you're doing.
     */
    public void update() {
        double now = glfwGetTime();
        double diff = now - time;
        deltaTime = (float) diff * speed;
        time = now;
        ++frames;
        incTime += diff;
        if (incTime >= 1.0) {
            lastFrames = frames;
            frames = 0;
            incTime = 0.0f;
        }
    }
}
