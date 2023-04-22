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

import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import java.util.Iterator;

public class UTF32Str {
    private static class Iter implements Iterator<Integer> {
        private final ByteBuffer buffer;
        private final int size;
        private int index;

        Iter(final byte[] bytes) {
            buffer = ByteBuffer.wrap(bytes);
            size = bytes.length / 4;
            index = 0;
        }

        @Override
        public boolean hasNext() {
            return index < size;
        }

        @Override
        public Integer next() {
            //Java has broken naming: index does not actually mean index but instead means byte offset!!!
            int c = buffer.getInt(index * 4);
            ++index;
            return c;
        }

        private void reset() {
            index = 0;
        }
    }

    private final Iter iter;

    /**
     * Creates a new UTF-32 string.
     * @param str input Java string.
     * @throws UnsupportedEncodingException if the UTF-32 encoding is not available on the current system.
     */
    public UTF32Str(final String str) throws UnsupportedEncodingException {
        byte[] bytes = str.getBytes("UTF-32");
        iter = new Iter(bytes);
    }

    /**
     * @return an iterator over the UTF-32 characters in this string.
     */
    public Iterator<Integer> iterator() {
        iter.reset();
        return iter;
    }
}
