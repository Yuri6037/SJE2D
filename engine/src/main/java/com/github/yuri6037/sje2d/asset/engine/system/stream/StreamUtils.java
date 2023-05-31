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

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;

public final class StreamUtils {
    private StreamUtils() {
    }

    /**
     * Checks if the given stream is seekable.
     * @param stream the asset stream to check.
     * @return true if this asset stream is seekable, false otherwise.
     */
    public static boolean isSeekable(final IAssetStream stream) {
        return stream instanceof ISeekableAssetStream;
    }

    /**
     * Copies an IAssetStream to a Java OutputStream.
     * @param inStream the IAssetStream to copy from.
     * @param outStream the Java OutputStream to copy to.
     * @throws IOException if a read operation failed.
     */
    public static void copy(final IAssetStream inStream, final OutputStream outStream) throws IOException {
        byte[] buffer = new byte[8192];
        int len;
        while ((len = inStream.read(buffer)) > 0) {
            outStream.write(buffer, 0, len);
        }
    }

    /**
     * Converts an IAssetStream to a Java InputStream.
     * @param stream the IAssetStream to convert.
     * @return an instance of a Java InputStream.
     */
    public static InputStream makeInputStream(final IAssetStream stream) {
        if (stream instanceof AssetInputStream) {
            return ((AssetInputStream) stream).getStream();
        }
        return new InputStreamWrapper(stream);
    }

    /**
     * Converts an IAssetStream to an ISeekableAssetStream.
     * @param stream the IAssetStream to convert.
     * @return an instance of an ISeekableAssetStream.
     * @throws IOException if the stream couldn't be converted to a seekable stream.
     */
    public static ISeekableAssetStream makeSeekable(final IAssetStream stream) throws IOException {
        if (isSeekable(stream)) {
            return (ISeekableAssetStream) stream;
        }
        File f = File.createTempFile("asset_stream_data", null);
        Files.copy(StreamUtils.makeInputStream(stream), f.toPath());
        return new SeekableFileStream(f);
    }
}
