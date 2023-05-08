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

package com.github.yuri6037.sje2d.window;

import com.github.yuri6037.sje2d.Application;
import com.github.yuri6037.sje2d.input.Key;
import com.github.yuri6037.sje2d.util.Bootstrap;
import org.lwjgl.opengl.GL;
import org.lwjgl.system.Callback;
import org.lwjgl.system.MemoryUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

//CHECKSTYLE OFF: AvoidStarImport
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
//CHECKSTYLE ON

public final class Window implements AutoCloseable {
    private static final Logger LOGGER = LoggerFactory.getLogger(Application.class);
    private final long window;
    private int width;
    private int height;
    private IInputHandler inputHandler;

    /**
     * Creates a new window from configuration.
     * @param config the window configuration.
     * @throws WindowException if the window or the OpenGL context could not be created.
     */
    public Window(final IWindowConfig config) throws WindowException {
        if (!glfwInit()) {
            throw new WindowException();
        }
        glfwDefaultWindowHints();
        glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE);
        glfwWindowHint(GLFW_RESIZABLE, GLFW_TRUE);
        glfwWindowHint(GLFW_CONTEXT_VERSION_MAJOR, 1);
        glfwWindowHint(GLFW_CONTEXT_VERSION_MINOR, 2);
        window = glfwCreateWindow(config.getWidth(), config.getHeight(), config.getTitle(), MemoryUtil.NULL,
                MemoryUtil.NULL);
        if (window == MemoryUtil.NULL) {
            throw new WindowException();
        }
        setupEventHandlers(config);
        glfwMakeContextCurrent(window);
        GL.createCapabilities();
        refreshGL(config.getWidth(), config.getHeight());
        glfwShowWindow(window);
        if (config.isVsync()) {
            setVsync(true);
        }
        int[] value = new int[1];
        glGetIntegerv(GL_MAX_TEXTURE_SIZE, value);
        LOGGER.info("Maximum texture size: {}", value[0]);
    }

    /**
     * Sets the input handler. Set to null to disable input handling.
     * @param handler the input handler to bind.
     */
    public void setInputHandler(final IInputHandler handler) {
        this.inputHandler = handler;
    }

    /**
     * @return the width of the window.
     */
    public int getWidth() {
        return width;
    }

    /**
     * @return the height of the window.
     */
    public int getHeight() {
        return height;
    }

    private void refreshGL(final int newWidth, final int newHeight) {
        width = newWidth;
        height = newHeight;
        if (Bootstrap.isMacOS()) {
            glViewport(0, 0, newWidth * 2, newHeight * 2);
        } else {
            glViewport(0, 0, newWidth, newHeight);
        }
        glPushMatrix();
        glEnable(GL_TEXTURE_2D);
        glEnable(GL_BLEND);
        glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
        glMatrixMode(GL_PROJECTION);
        glLoadIdentity();
        glOrtho(0, newWidth, newHeight, 0, 1, -1);
        glMatrixMode(GL_MODELVIEW);
    }

    /**
     * Enables or disables vsync. The vsync option allows to limit the frame-rate to the refresh rate of the
     * physical display. This is used to avoid screen tearing.
     * @param flag true to enable vsync, false otherwise.
     */
    public void setVsync(final boolean flag) {
        if (flag) {
            glfwSwapInterval(1);
        } else {
            glfwSwapInterval(0);
        }
    }

    private void handleCallback(final Callback callback) {
        if (callback != null) {
            callback.close();
        }
    }

    private void handleKey(final Key key, final int action) {
        switch (action) {
            case GLFW_PRESS -> inputHandler.onKeyPress(key);
            case GLFW_RELEASE -> inputHandler.onKeyRelease(key);
        }
    }

    private void setupEventHandlers(final IWindowConfig config) {
        //CHECKSTYLE OFF: HiddenField
        handleCallback(glfwSetWindowSizeCallback(window, (window, width, height) -> refreshGL(width, height)));
        handleCallback(glfwSetKeyCallback(window, (window, key, scancode, action, mods) -> {
            if (key == GLFW_KEY_ESCAPE && action == GLFW_RELEASE) {
                glfwSetWindowShouldClose(window, true);
            }
        }));
        if (config.supportsKeyboard()) {
            handleCallback(glfwSetCharCallback(window, (window, code) -> {
                if (inputHandler != null) {
                    String text = Character.toString(code);
                    inputHandler.onTextInput(text);
                }
            }));
            handleCallback(glfwSetKeyCallback(window, (window, key, scancode, action, mods) -> {
                if (inputHandler != null) {
                    Key k = Key.fromGlfw(key);
                    handleKey(k, action);
                }
            }));
        }
        if (config.supportsMouse()) {
            handleCallback(glfwSetMouseButtonCallback(window, ((window, button, action, mods) -> {
                if (inputHandler != null) {
                    Key k = switch (button) {
                        case GLFW_MOUSE_BUTTON_LEFT -> Key.MOUSE_LEFT;
                        case GLFW_MOUSE_BUTTON_RIGHT -> Key.MOUSE_RIGHT;
                        default -> Key.MOUSE_MIDDLE;
                    };
                    handleKey(k, action);
                }
            })));
            handleCallback(glfwSetCursorPosCallback(window, (window, x, y) -> {
                if (inputHandler != null) {
                    inputHandler.onAxisUpdate(AxisKey.MOUSE, (float) x, (float) y, 0);
                }
            }));
            handleCallback(glfwSetScrollCallback(window, ((window1, xoffset, yoffset) -> {
                if (inputHandler != null) {
                    inputHandler.onAxisUpdate(AxisKey.MOUSE_WHEEL, (float) xoffset, (float) yoffset, 0);
                }
            })));
        }
        //CHECKSTYLE ON
    }

    @Override
    public void close() {
        glfwTerminate();
    }

    /**
     * Sets a flag to close the window.
     */
    public void requestClose() {
        LOGGER.debug("Window is about to close.");
        glfwSetWindowShouldClose(window, true);
    }

    /**
     * @return true if the window should be closing, false otherwise.
     */
    public boolean shouldClose() {
        return glfwWindowShouldClose(window);
    }

    /**
     * Updates this window. This function swaps buffers, polls events and clears the color buffer.
     * Call this function at the end of each frame.
     * WARNING: Do not call this method if you're not the direct owner of the timer,
     * unless you know what you're doing.
     */
    public void update() {
        glfwSwapBuffers(window);
        glfwPollEvents();
        glClear(GL_COLOR_BUFFER_BIT);
    }
}
