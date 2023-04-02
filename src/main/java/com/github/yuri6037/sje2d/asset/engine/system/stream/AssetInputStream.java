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

package com.github.yuri6037.sje2d.asset.engine.system.stream;

import java.io.IOException;
import java.io.InputStream;

public abstract class AssetInputStream implements IAssetStream {
    private final InputStream stream;

    private static final AssetInputStream NULL_STREAM = new AssetInputStream(InputStream.nullInputStream()) {
        @Override
        public String getMimeType() {
            return null;
        }
    };

    /**
     * @return a null AssetInputStream which can be used in tests.
     */
    public static AssetInputStream nullStream() {
        return NULL_STREAM;
    }

    /**
     * Creates a new BaseInputStream from an existing InputStream.
     * @param stream the InputStream to wrap.
     */
    public AssetInputStream(final InputStream stream) {
        this.stream = stream;
    }

    /**
     * @return the underlying input stream.
     */
    public InputStream getStream() {
        return stream;
    }

    @Override
    public final int read() throws IOException {
        return stream.read();
    }

    @Override
    public final void close() throws IOException {
        stream.close();
    }

    @Override
    public final int read(final byte[] buffer) throws IOException {
        return stream.read(buffer);
    }

    @Override
    public final int read(final byte[] buffer, final int off, final int len) throws IOException {
        return stream.read(buffer, off, len);
    }
}
