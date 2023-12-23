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

package com.github.yuri6037.sje2d.util;

import java.util.ArrayList;

public final class Scheduler {
    private static final class Function {
        private double time;
        private final double delta;
        private final Runnable runnable;

        Function(final double time, final double delta, final Runnable runnable) {
            this.time = time;
            this.delta = delta;
            this.runnable = runnable;
        }
    }

    private final ArrayList<Function> functions = new ArrayList<>();
    private final ArrayList<Function> timedFunctions = new ArrayList<>();
    private final Timer timer;

    /**
     * Creates a new Scheduler.
     * @param timer the timer this scheduler should be linked to.
     */
    public Scheduler(final Timer timer) {
        this.timer = timer;
    }

    /**
     * Schedules a new Runnable to run after a certain number of seconds.
     * @param delta the number of seconds from now at which to run the Runnable.
     * @param runnable the Runnable to run after delta seconds.
     * @return the runnable passed as argument to simplify cancelling functions.
     */
    public Runnable schedule(final float delta, final Runnable runnable) {
        return schedule(delta, false, runnable);
    }

    /**
     * Schedules a new Runnable to run after a certain number of seconds.
     * @param delta the number of seconds from now at which to run the Runnable.
     * @param repeat true to periodically run the function every time delta seconds is reached, false to run only once.
     * @param runnable the Runnable to run after delta seconds.
     * @return the runnable passed as argument to simplify cancelling functions.
     */
    public Runnable schedule(final float delta, final boolean repeat, final Runnable runnable) {
        if (repeat) {
            timedFunctions.add(new Function(timer.getTime() + delta, delta, runnable));
        } else {
            functions.add(new Function(timer.getTime() + delta, delta, runnable));
        }
        return runnable;
    }

    /**
     * Cancel the execution of a Runnable.
     * @param runnable the runnable to cancel.
     */
    public void cancel(final Runnable runnable) {
        Function function = null;
        for (Function f: functions) {
            if (f.runnable == runnable) {
                function = f;
            }
        }
        if (function == null) {
            for (Function f : timedFunctions) {
                if (f.runnable == runnable) {
                    function = f;
                }
            }
        }
        if (function != null) {
            functions.remove(function);
        }
    }

    /**
     * Updates the Scheduler and automatically run any scheduled function if needed.
     * Call this function each cycle in the main loop.
     */
    public void update() {
        for (Function f: timedFunctions) {
            if (timer.getTime() >= f.time) {
                f.runnable.run();
                f.time = timer.getTime() + f.delta;
            }
        }
        for (int i = functions.size() - 1; i != -1; --i) {
            Function f = functions.get(i);
            if (timer.getTime() >= f.time) {
                f.runnable.run();
                functions.remove(i);
            }
        }
    }
}
