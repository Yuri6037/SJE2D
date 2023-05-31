/*
 * Copyright (c) 2023, SJE2D
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

package com.github.yuri6037.sje2d.asset.engine.system.stream;

import java.io.Closeable;
import java.io.IOException;

public interface IInputStream extends Closeable {
    /**
     * Reads a single byte of data from the stream.
     * @return an unsigned byte value [0;255]
     * @throws IOException if the read operation failed.
     */
    int read() throws IOException;

    /**
     * Reads a buffer of bytes from the stream.
     * @param buffer the destination buffer to read into.
     * @return the total number of bytes read into the buffer, or -1 if there is no more data because the end of
     * the stream has been reached.
     * @throws IOException if the read operation failed.
     */
    int read(byte[] buffer) throws IOException;

    /**
     * Reads a buffer of bytes from the stream.
     * @param buffer the destination buffer to read into.
     * @param off the offset in the destination buffer.
     * @param len the number of bytes to read from the stream.
     * @return the total number of bytes read into the buffer, or -1 if there is no more data because the end of
     * the stream has been reached.
     * @throws IOException if the read operation failed.
     */
    int read(byte[] buffer, int off, int len) throws IOException;
}
