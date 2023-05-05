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

package com.github.yuri6037.sje2d.util;

import java.util.HashMap;

public final class StringEnum<T> {
    private final HashMap<String, T> map;

    private StringEnum(final HashMap<String, T> map) {
        this.map = map;
    }

    /**
     * Gets an enum from its string value.
     * @param def the default enum value to return.
     * @param name the name of the enum to search for.
     * @return the corresponding enum or the default enum if not found.
     */
    public T get(final T def, final String name) {
        if (name == null) {
            return def;
        }
        T e = map.get(name.toLowerCase());
        return e == null ? def : e;
    }

    /**
     * Create a new string enum.
     * @param objects a list of (String, E) tuples.
     * @return a new instance of a StringEnum
     * @param <T> the enum type.
     */
    public static <T> StringEnum<T> create(final Object... objects) {
        HashMap<String, T> map = new HashMap<>();
        if (objects.length % 2 != 0) {
            throw new IllegalArgumentException("This function expects a list of (String, E) tuples");
        }
        for (int i = 0; i != objects.length - 2; i += 2) {
            try {
                String str = (String) objects[i];
                //This is safe because 1: this is only intended to be used at init time,
                // 2: this catches class cast exceptions.
                @SuppressWarnings("unchecked") T e = (T) objects[i + 1];
                map.put(str.toLowerCase(), e);
            } catch (ClassCastException e) {
                throw new IllegalArgumentException("This function expects a list of (String, E) tuples", e);
            }
        }
        return new StringEnum<>(map);
    }
}
