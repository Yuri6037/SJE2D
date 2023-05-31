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

package com.github.yuri6037.sje2d.test;

import com.github.yuri6037.sje2d.asset.engine.AssetURL;
import org.junit.Assert;
import org.junit.Test;

import java.net.MalformedURLException;

public class TestAssetURL {
    /**
     * Test that AssetURL can represent basic HTTP links.
     * @throws MalformedURLException if the test failed.
     */
    @Test
    public void basicHttp() throws MalformedURLException {
        AssetURL url = new AssetURL("https://github.com");
        Assert.assertNull(url.getMimeType());
        Assert.assertEquals("https", url.getProtocol());
        Assert.assertEquals("github.com", url.getPath());
        Assert.assertNull(url.getParameter("test"));
    }

    /**
     * Test that AssetURL can represent basic local file links.
     * @throws MalformedURLException if the test failed.
     */
    @Test
    public void basicFile() throws MalformedURLException {
        AssetURL url = new AssetURL("file:///etc/file.txt");
        Assert.assertNull(url.getMimeType());
        Assert.assertEquals("file", url.getProtocol());
        Assert.assertEquals("/etc/file.txt", url.getPath());
        Assert.assertNull(url.getParameter("test"));
    }

    /**
     * Test that AssetURL can represent basic local file links with query parameters.
     * @throws MalformedURLException if the test failed.
     */
    @Test
    public void paramsFile() throws MalformedURLException {
        AssetURL url = new AssetURL("file:///etc/file.txt?name=name1");
        Assert.assertNull(url.getMimeType());
        Assert.assertEquals("file", url.getProtocol());
        Assert.assertEquals("/etc/file.txt", url.getPath());
        Assert.assertEquals("name1", url.getParameter("name"));
    }

    /**
     * Test that AssetURL can represent basic local file links with query parameters and a custom pre-pended mime-type.
     * @throws MalformedURLException if the test failed.
     */
    @Test
    public void mimeTypeFile() throws MalformedURLException {
        AssetURL url = new AssetURL("config/text file:///etc/file.txt?name=name1");
        Assert.assertEquals("config/text", url.getMimeType());
        Assert.assertEquals("file", url.getProtocol());
        Assert.assertEquals("/etc/file.txt", url.getPath());
        Assert.assertEquals("name1", url.getParameter("name"));
    }

    /**
     * Test that AssetURL can represent a path with spaces with no URL formatting.
     * @throws MalformedURLException if the test failed.
     */
    @Test
    public void withSpaces() throws MalformedURLException {
        AssetURL url = new AssetURL("test://this is a test?name=basic");
        Assert.assertNull(url.getMimeType());
        Assert.assertEquals("test", url.getProtocol());
        Assert.assertEquals("this is a test", url.getPath());
        Assert.assertEquals("basic", url.getParameter("name"));
    }
}
