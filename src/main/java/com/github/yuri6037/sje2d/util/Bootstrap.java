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

import org.lwjgl.system.macosx.LibC;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.lang.management.ManagementFactory;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public final class Bootstrap {
    private Bootstrap() {
    }

    private static final Logger LOGGER = LoggerFactory.getLogger(Bootstrap.class);
    private static final String BOOTSTRAP_PROP = "com.github.yuri6037.sje2d.bootstrap";

    /**
     * @return true if the current running operating system is macOS.
     */
    public static boolean isMacOS() {
        String prop = System.getProperty("os.name", "unknown").toLowerCase();
        return prop.contains("mac") || prop.contains("darwin");
    }

    /**
     * @return true if the JVM was started on the main thread, false otherwise.
     */
    public static boolean isOnMainThread() {
        if (!isMacOS()) {
            return true; //No need of any GLFW hacks on other platforms
        }
        long pid = LibC.getpid();
        return "1".equals(System.getenv("JAVA_STARTED_ON_FIRST_THREAD_" + pid))
                && "true".equals(System.getProperty("java.awt.headless"));
    }

    /**
     * @return true if the JVM was started by the bootstrap, false otherwise.
     */
    public static boolean isBootstrapped() {
        if (!isMacOS()) {
            return true; //No need of any GLFW hacks on other platforms
        }
        return "1".equals(System.getProperty(BOOTSTRAP_PROP));
    }

    /**
     * @return the main class of the application which is being bootstrapped.
     */
    public static String getMainClass() {
        long pid = LibC.getpid();
        String mainClass = System.getenv("JAVA_MAIN_CLASS_" + pid);
        if (mainClass == null) {
            StackTraceElement[] trace = Thread.currentThread().getStackTrace();
            if (trace.length > 0) {
                mainClass = trace[trace.length - 1].getClassName();
            }
        }
        return mainClass;
    }

    /**
     * Restarted the JVM on the main thread.
     * @param args the program arguments.
     */
    public static void startOnMainThread(final String[] args) {
        if (!isMacOS()) {
            return; //No need of any GLFW hacks on other platforms
        }
        //Arrays.asList is a peace of shit: it causes UnsupportedOperationException
        List<String> cmd = new ArrayList<>(Arrays.asList(System.getProperty("java.home") + File.separator
                + "bin" + File.separator + "java", "-XstartOnFirstThread", "-Djava.awt.headless=true",
                "-D" + BOOTSTRAP_PROP + "=1"));
        cmd.addAll(ManagementFactory.getRuntimeMXBean().getInputArguments());
        cmd.add("-cp");
        cmd.add(ManagementFactory.getRuntimeMXBean().getClassPath());
        String mainClass = getMainClass();
        if (mainClass == null) {
            throw new RuntimeException("Unable to locate application main class");
        }
        cmd.add(mainClass);
        cmd.addAll(Arrays.asList(args));
        try {
            LOGGER.debug("Running: {}", String.join(" ", cmd));
            new ProcessBuilder(cmd).inheritIO().start().waitFor();
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }
        System.exit(0);
    }
}
