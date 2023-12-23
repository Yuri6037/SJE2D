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

public class Delegate<P> {
    public interface IFunction<P> {
        /**
         * Called when the delegate is called.
         * @param parameter the parameter which was passed to the delegate.
         */
        void run(P parameter);
    }

    private final ArrayList<IFunction<P>> functions = new ArrayList<>();

    /**
     * Adds a new function to this delegate.
     * @param function the function to add.
     * @return the newly added function to simplify removal.
     */
    public IFunction<P> add(final IFunction<P> function) {
        this.functions.add(function);
        return function;
    }

    /**
     * Removes a function from this delegate.
     * @param function the function to remove.
     */
    public void remove(final IFunction<P> function) {
        this.functions.remove(function);
    }

    /**
     * Calls this delegate.
     * @param parameter a parameter to pass to each delegate function.
     */
    public void call(final P parameter) {
        for (IFunction<P> function: functions) {
            function.run(parameter);
        }
    }
}
