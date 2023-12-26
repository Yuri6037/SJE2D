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

package com.github.yuri6037.sje2d.math;

public record Vector(double x, double y) {
    /**
     * Adds two vectors.
     * @param other the other vector.
     * @return the result.
     */
    public Vector add(final Vector other) {
        return new Vector(x + other.x, y + other.y);
    }

    /**
     * Multiplies two vectors.
     * @param other the other vector.
     * @return the result.
     */
    public Vector mul(final Vector other) {
        return new Vector(x * other.x, y * other.y);
    }

    /**
     * Multiplies a vector by a scalar.
     * @param other the other scalar.
     * @return the result.
     */
    public Vector mul(final double other) {
        return new Vector(x * other, y * other);
    }

    /**
     * Subtracts two vectors.
     * @param other the other vector.
     * @return the result.
     */
    public Vector sub(final Vector other) {
        return new Vector(x - other.x, y - other.y);
    }

    /**
     * Divides two vectors.
     * @param other the other vector.
     * @return the result.
     */
    public Vector div(final Vector other) {
        return new Vector(x / other.x, y / other.y);
    }

    /**
     * Computes the dot product between two vectors.
     * @param other the other vector.
     * @return the result.
     */
    public double dot(final Vector other) {
        return x * other.x + y * other.y;
    }

    /**
     * @return the norm of this vector.
     */
    public double norm() {
        return Math.sqrt(x * x + y * y);
    }

    /**
     * @return the squared norm of this vector.
     */
    public double normSquared() {
        return x * x + y * y;
    }

    /**
     * Returns the distance between two vectors.
     * @param a the first operand.
     * @param b the second operand.
     * @return the distance.
     */
    public static double distance(final Vector a, final Vector b) {
        return b.sub(a).norm();
    }

    /**
     * Returns the distance between two vectors.
     * @param a the first operand.
     * @param b the second operand.
     * @return the squared distance.
     */
    public static double distanceSquared(final Vector a, final Vector b) {
        return b.sub(a).normSquared();
    }

    /**
     * @return a normalized copy of this vector.
     */
    public Vector normalize() {
        double n = norm();
        return new Vector(x / n, y / n);
    }

    /**
     * Compares this vector to another.
     * @param other the other operand.
     * @return true if this is less than other, false otherwise.
     */
    public boolean lt(final Vector other) {
        return x < other.x && y < other.y;
    }

    /**
     * Compares this vector to another.
     * @param other the other operand.
     * @return true if this is greater than other, false otherwise.
     */
    public boolean gt(final Vector other) {
        return x > other.x && y > other.y;
    }

    /**
     * Compares this vector to another.
     * @param other the other operand.
     * @return true if this is less or equal than other, false otherwise.
     */
    public boolean le(final Vector other) {
        return x <= other.x && y <= other.y;
    }

    /**
     * Compares this vector to another.
     * @param other the other operand.
     * @return true if this is greater or equal than other, false otherwise.
     */
    public boolean ge(final Vector other) {
        return x >= other.x && y >= other.y;
    }
}
