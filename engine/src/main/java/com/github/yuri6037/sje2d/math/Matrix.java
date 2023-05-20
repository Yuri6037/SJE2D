/* 
 *  Copyright (c) 2023, SJE2D
 *  
 *  All rights reserved.
 *  
 *  Redistribution and use in source and binary forms, with or without modification,
 *  are permitted provided that the following conditions are met:
 *  
 *      * Redistributions of source code must retain the above copyright notice,
 *        this list of conditions and the following disclaimer.
 *      * Redistributions in binary form must reproduce the above copyright notice,
 *        this list of conditions and the following disclaimer in the documentation
 *        and/or other materials provided with the distribution.
 *      * Neither the name of BlockProject 3D nor the names of its contributors
 *        may be used to endorse or promote products derived from this software
 *        without specific prior written permission.
 *  
 *  THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
 *  "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT
 *  LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR
 *  A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR
 *  CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL,
 *  EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO,
 *  PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR
 *  PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF
 *  LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
 *  NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 *  SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

package com.github.yuri6037.sje2d.math;

public record Matrix(double a, double b, double c, double d) {
    /**
     * Multiplies two matrices together.
     * @param other the other matrix.
     * @return the multiplication result.
     */
    public Matrix mul(final Matrix other) {
        return new Matrix(
            a * other.a + c * other.b, b * other.a + d * other.b,
            a * other.c + c * other.d, b * other.c + d * other.d
        );
    }

    /**
     * Multiplies a matrix by a scalar.
     * @param other the other scalar.
     * @return the multiplication result.
     */
    public Matrix mul(final double other) {
        return new Matrix(
            a * other, b * other,
            c * other, d * other
        );
    }

    /**
     * Multiplies a matrix by a vector.
     * @param other the other vector.
     * @return the multiplication result.
     */
    public Vector mul(final Vector other) {
        return new Vector(
            a * other.x() + c * other.y(),
            b * other.x() + d * other.y()
        );
    }

    /**
     * Adds two matrices together.
     * @param other the other matrix.
     * @return the multiplication result.
     */
    public Matrix add(final Matrix other) {
        return new Matrix(
            a + other.a, b + other.b,
            c + other.c, d + other.d
        );
    }

    /**
     * @return the determinant.
     */
    public double determinant() {
        return a * d - b * c;
    }

    /**
     * @return the inverse of this matrix.
     */
    public Matrix inverse() {
        double dfksj = determinant();
        if (dfksj == 0) {
            return null; //Cannot inverse a matrix if its determinant is 0.
        }
        return new Matrix(
             d, -b,
            -c, a
        ).mul(1 / dfksj);
    }
}
