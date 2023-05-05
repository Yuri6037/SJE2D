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

package com.github.yuri6037.sje2d.input;

import java.util.HashMap;

//CHECKSTYLE OFF: AvoidStarImport
import static org.lwjgl.glfw.GLFW.*;
//CHECKSTYLE ON

public final class Key {
//CHECKSTYLE OFF: JavadocVariable
    public static final Key SPACE;
    public static final Key APOSTROPHE;
    public static final Key COMMA;
    public static final Key MINUS;
    public static final Key PERIOD;
    public static final Key SLASH;
    public static final Key NUM_0;
    public static final Key NUM_1;
    public static final Key NUM_2;
    public static final Key NUM_3;
    public static final Key NUM_4;
    public static final Key NUM_5;
    public static final Key NUM_6;
    public static final Key NUM_7;
    public static final Key NUM_8;
    public static final Key NUM_9;
    public static final Key SEMICOLON;
    public static final Key EQUAL;
    public static final Key A;
    public static final Key B;
    public static final Key C;
    public static final Key D;
    public static final Key E;
    public static final Key F;
    public static final Key G;
    public static final Key H;
    public static final Key I;
    public static final Key J;
    public static final Key K;
    public static final Key L;
    public static final Key M;
    public static final Key N;
    public static final Key O;
    public static final Key P;
    public static final Key Q;
    public static final Key R;
    public static final Key S;
    public static final Key T;
    public static final Key U;
    public static final Key V;
    public static final Key W;
    public static final Key X;
    public static final Key Y;
    public static final Key Z;
    public static final Key LEFT_BRACKET;
    public static final Key BACKSLASH;
    public static final Key RIGHT_BRACKET;
    public static final Key GRAVE_ACCENT;
    public static final Key WORLD_1;
    public static final Key WORLD_2;
    public static final Key ESCAPE;
    public static final Key ENTER;
    public static final Key TAB;
    public static final Key BACKSPACE;
    public static final Key INSERT;
    public static final Key DELETE;
    public static final Key RIGHT;
    public static final Key LEFT;
    public static final Key DOWN;
    public static final Key UP;
    public static final Key PAGE_UP;
    public static final Key PAGE_DOWN;
    public static final Key HOME;
    public static final Key END;
    public static final Key CAPS_LOCK;
    public static final Key SCROLL_LOCK;
    public static final Key NUM_LOCK;
    public static final Key PRINT_SCREEN;
    public static final Key PAUSE;
    public static final Key F1;
    public static final Key F2;
    public static final Key F3;
    public static final Key F4;
    public static final Key F5;
    public static final Key F6;
    public static final Key F7;
    public static final Key F8;
    public static final Key F9;
    public static final Key F10;
    public static final Key F11;
    public static final Key F12;
    public static final Key F13;
    public static final Key F14;
    public static final Key F15;
    public static final Key F16;
    public static final Key F17;
    public static final Key F18;
    public static final Key F19;
    public static final Key F20;
    public static final Key F21;
    public static final Key F22;
    public static final Key F23;
    public static final Key F24;
    public static final Key F25;
    public static final Key KP_0;
    public static final Key KP_1;
    public static final Key KP_2;
    public static final Key KP_3;
    public static final Key KP_4;
    public static final Key KP_5;
    public static final Key KP_6;
    public static final Key KP_7;
    public static final Key KP_8;
    public static final Key KP_9;
    public static final Key KP_DECIMAL;
    public static final Key KP_DIVIDE;
    public static final Key KP_MULTIPLY;
    public static final Key KP_SUBTRACT;
    public static final Key KP_ADD;
    public static final Key KP_ENTER;
    public static final Key KP_EQUAL;
    public static final Key LEFT_SHIFT;
    public static final Key LEFT_CONTROL;
    public static final Key LEFT_ALT;
    public static final Key LEFT_SUPER;
    public static final Key RIGHT_SHIFT;
    public static final Key RIGHT_CONTROL;
    public static final Key RIGHT_ALT;
    public static final Key RIGHT_SUPER;
    public static final Key MENU;
    public static final Key MOUSE_LEFT;
    public static final Key MOUSE_RIGHT;
    public static final Key MOUSE_MIDDLE;
//CHECKSTYLE ON

    private final String name;
    private final int id;
    private static final HashMap<String, Key> STRING_KEY_MAP;
    private static final HashMap<Integer, Key> GLFW_KEY_MAP;

    private Key(final int id, final String name) {
        this.id = id;
        this.name = name;
        STRING_KEY_MAP.put(name, this);
        GLFW_KEY_MAP.put(id, this);
    }

    /**
     * Gets the abstract key corresponding to a GLFW key.
     * @param key the GLFW key index.
     * @return the corresponding SJE2D key or null if not found.
     */
    public static Key fromGlfw(final int key) {
        return GLFW_KEY_MAP.get(key);
    }

    /**
     * Gets the abstract key from a key name.
     * @param name the name of the key.
     * @return the matched SJE2D key or null if not found.
     */
    public static Key fromString(final String name) {
        return STRING_KEY_MAP.get(name);
    }

    /**
     * @return the name of the key in the current system's language.
     */
    public String getName() {
        String n = glfwGetKeyName(id, 0);
        return n == null ? getStaticName() : n;
    }

    /**
     * @return the static name of the key.
     */
    public String getStaticName() {
        return name;
    }

    static {
        STRING_KEY_MAP = new HashMap<>();
        GLFW_KEY_MAP = new HashMap<>();
        SPACE = new Key(GLFW_KEY_SPACE, "SPACE");
        APOSTROPHE = new Key(GLFW_KEY_APOSTROPHE, "APOSTROPHE");
        COMMA = new Key(GLFW_KEY_COMMA, "COMMA");
        MINUS = new Key(GLFW_KEY_MINUS, "MINUS");
        PERIOD = new Key(GLFW_KEY_PERIOD, "PERIOD");
        SLASH = new Key(GLFW_KEY_SLASH, "SLASH");
        NUM_0 = new Key(GLFW_KEY_0, "0");
        NUM_1 = new Key(GLFW_KEY_1, "1");
        NUM_2 = new Key(GLFW_KEY_2, "2");
        NUM_3 = new Key(GLFW_KEY_3, "3");
        NUM_4 = new Key(GLFW_KEY_4, "4");
        NUM_5 = new Key(GLFW_KEY_5, "5");
        NUM_6 = new Key(GLFW_KEY_6, "6");
        NUM_7 = new Key(GLFW_KEY_7, "7");
        NUM_8 = new Key(GLFW_KEY_8, "8");
        NUM_9 = new Key(GLFW_KEY_9, "9");
        SEMICOLON = new Key(GLFW_KEY_SEMICOLON, "SEMICOLON");
        EQUAL = new Key(GLFW_KEY_EQUAL, "EQUAL");
        A = new Key(GLFW_KEY_A, "A");
        B = new Key(GLFW_KEY_B, "B");
        C = new Key(GLFW_KEY_C, "C");
        D = new Key(GLFW_KEY_D, "D");
        E = new Key(GLFW_KEY_E, "E");
        F = new Key(GLFW_KEY_F, "F");
        G = new Key(GLFW_KEY_G, "G");
        H = new Key(GLFW_KEY_H, "H");
        I = new Key(GLFW_KEY_I, "I");
        J = new Key(GLFW_KEY_J, "J");
        K = new Key(GLFW_KEY_K, "K");
        L = new Key(GLFW_KEY_L, "L");
        M = new Key(GLFW_KEY_M, "M");
        N = new Key(GLFW_KEY_N, "N");
        O = new Key(GLFW_KEY_O, "O");
        P = new Key(GLFW_KEY_P, "P");
        Q = new Key(GLFW_KEY_Q, "Q");
        R = new Key(GLFW_KEY_R, "R");
        S = new Key(GLFW_KEY_S, "S");
        T = new Key(GLFW_KEY_T, "T");
        U = new Key(GLFW_KEY_U, "U");
        V = new Key(GLFW_KEY_V, "V");
        W = new Key(GLFW_KEY_W, "W");
        X = new Key(GLFW_KEY_X, "X");
        Y = new Key(GLFW_KEY_Y, "Y");
        Z = new Key(GLFW_KEY_Z, "Z");
        LEFT_BRACKET = new Key(GLFW_KEY_LEFT_BRACKET, "LEFT_BRACKET");
        BACKSLASH = new Key(GLFW_KEY_BACKSLASH, "BACKSLASH");
        RIGHT_BRACKET = new Key(GLFW_KEY_RIGHT_BRACKET, "RIGHT_BRACKET");
        GRAVE_ACCENT = new Key(GLFW_KEY_GRAVE_ACCENT, "GRAVE_ACCENT");
        WORLD_1 = new Key(GLFW_KEY_WORLD_1, "WORLD_1");
        WORLD_2 = new Key(GLFW_KEY_WORLD_2, "WORLD_2");
        ESCAPE = new Key(GLFW_KEY_ESCAPE, "ESCAPE");
        ENTER = new Key(GLFW_KEY_ENTER, "ENTER");
        TAB = new Key(GLFW_KEY_TAB, "TAB");
        BACKSPACE = new Key(GLFW_KEY_BACKSPACE, "BACKSPACE");
        INSERT = new Key(GLFW_KEY_INSERT, "INSERT");
        DELETE = new Key(GLFW_KEY_DELETE, "DELETE");
        RIGHT = new Key(GLFW_KEY_RIGHT, "RIGHT");
        LEFT = new Key(GLFW_KEY_LEFT, "LEFT");
        DOWN = new Key(GLFW_KEY_DOWN, "DOWN");
        UP = new Key(GLFW_KEY_UP, "UP");
        PAGE_UP = new Key(GLFW_KEY_PAGE_UP, "PAGE_UP");
        PAGE_DOWN = new Key(GLFW_KEY_PAGE_DOWN, "PAGE_DOWN");
        HOME = new Key(GLFW_KEY_HOME, "HOME");
        END = new Key(GLFW_KEY_END, "END");
        CAPS_LOCK = new Key(GLFW_KEY_CAPS_LOCK, "CAPS_LOCK");
        SCROLL_LOCK = new Key(GLFW_KEY_SCROLL_LOCK, "SCROLL_LOCK");
        NUM_LOCK = new Key(GLFW_KEY_NUM_LOCK, "NUM_LOCK");
        PRINT_SCREEN = new Key(GLFW_KEY_PRINT_SCREEN, "PRINT_SCREEN");
        PAUSE = new Key(GLFW_KEY_PAUSE, "PAUSE");
        F1 = new Key(GLFW_KEY_F1, "F1");
        F2 = new Key(GLFW_KEY_F2, "F2");
        F3 = new Key(GLFW_KEY_F3, "F3");
        F4 = new Key(GLFW_KEY_F4, "F4");
        F5 = new Key(GLFW_KEY_F5, "F5");
        F6 = new Key(GLFW_KEY_F6, "F6");
        F7 = new Key(GLFW_KEY_F7, "F7");
        F8 = new Key(GLFW_KEY_F8, "F8");
        F9 = new Key(GLFW_KEY_F9, "F9");
        F10 = new Key(GLFW_KEY_F10, "F10");
        F11 = new Key(GLFW_KEY_F11, "F11");
        F12 = new Key(GLFW_KEY_F12, "F12");
        F13 = new Key(GLFW_KEY_F13, "F13");
        F14 = new Key(GLFW_KEY_F14, "F14");
        F15 = new Key(GLFW_KEY_F15, "F15");
        F16 = new Key(GLFW_KEY_F16, "F16");
        F17 = new Key(GLFW_KEY_F17, "F17");
        F18 = new Key(GLFW_KEY_F18, "F18");
        F19 = new Key(GLFW_KEY_F19, "F19");
        F20 = new Key(GLFW_KEY_F20, "F20");
        F21 = new Key(GLFW_KEY_F21, "F21");
        F22 = new Key(GLFW_KEY_F22, "F22");
        F23 = new Key(GLFW_KEY_F23, "F23");
        F24 = new Key(GLFW_KEY_F24, "F24");
        F25 = new Key(GLFW_KEY_F25, "F25");
        KP_0 = new Key(GLFW_KEY_KP_0, "KP_0");
        KP_1 = new Key(GLFW_KEY_KP_1, "KP_1");
        KP_2 = new Key(GLFW_KEY_KP_2, "KP_2");
        KP_3 = new Key(GLFW_KEY_KP_3, "KP_3");
        KP_4 = new Key(GLFW_KEY_KP_4, "KP_4");
        KP_5 = new Key(GLFW_KEY_KP_5, "KP_5");
        KP_6 = new Key(GLFW_KEY_KP_6, "KP_6");
        KP_7 = new Key(GLFW_KEY_KP_7, "KP_7");
        KP_8 = new Key(GLFW_KEY_KP_8, "KP_8");
        KP_9 = new Key(GLFW_KEY_KP_9, "KP_9");
        KP_DECIMAL = new Key(GLFW_KEY_KP_DECIMAL, "KP_DECIMAL");
        KP_DIVIDE = new Key(GLFW_KEY_KP_DIVIDE, "KP_DIVIDE");
        KP_MULTIPLY = new Key(GLFW_KEY_KP_MULTIPLY, "KP_MULTIPLY");
        KP_SUBTRACT = new Key(GLFW_KEY_KP_SUBTRACT, "KP_SUBTRACT");
        KP_ADD = new Key(GLFW_KEY_KP_ADD, "KP_ADD");
        KP_ENTER = new Key(GLFW_KEY_KP_ENTER, "KP_ENTER");
        KP_EQUAL = new Key(GLFW_KEY_KP_EQUAL, "KP_EQUAL");
        LEFT_SHIFT = new Key(GLFW_KEY_LEFT_SHIFT, "LEFT_SHIFT");
        LEFT_CONTROL = new Key(GLFW_KEY_LEFT_CONTROL, "LEFT_CONTROL");
        LEFT_ALT = new Key(GLFW_KEY_LEFT_ALT, "LEFT_ALT");
        LEFT_SUPER = new Key(GLFW_KEY_LEFT_SUPER, "LEFT_SUPER");
        RIGHT_SHIFT = new Key(GLFW_KEY_RIGHT_SHIFT, "RIGHT_SHIFT");
        RIGHT_CONTROL = new Key(GLFW_KEY_RIGHT_CONTROL, "RIGHT_CONTROL");
        RIGHT_ALT = new Key(GLFW_KEY_RIGHT_ALT, "RIGHT_ALT");
        RIGHT_SUPER = new Key(GLFW_KEY_RIGHT_SUPER, "RIGHT_SUPER");
        MENU = new Key(GLFW_KEY_MENU, "MENU");
        MOUSE_LEFT = new Key(GLFW_MOUSE_BUTTON_LEFT, "MOUSE_LEFT");
        MOUSE_RIGHT = new Key(GLFW_MOUSE_BUTTON_RIGHT, "MOUSE_RIGHT");
        MOUSE_MIDDLE = new Key(GLFW_MOUSE_BUTTON_MIDDLE, "MOUSE_MIDDLE");
    }
}
