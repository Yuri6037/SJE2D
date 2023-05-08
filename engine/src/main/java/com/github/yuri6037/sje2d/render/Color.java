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

package com.github.yuri6037.sje2d.render;

public record Color(float r, float g, float b, float a) {
    /**
     * Creates a new color from normalized RGB (0-1).
     * @param r red.
     * @param g green.
     * @param b blue.
     */
    public Color(final float r, final float g, final float b) {
        this(r, g, b, 1.0f);
    }

    /**
     * Creates a new color from a greyscale value.
     * @param greyscale value.
     */
    public Color(final float greyscale) {
        this(greyscale, greyscale, greyscale, 1.0f);
    }

    /**
     * Creates a new color from denormalized (0-255) RGBA components.
     * @param r red.
     * @param g green.
     * @param b blue.
     * @param a alpha.
     */
    public Color(final int r, final int g, final int b, final int a) {
        this((float) r / 255.0f, (float) g / 255.0f, (float) b / 255.0f, (float) a / 255.0f);
    }

    /**
     * Creates a new color from denormalized (0-255) RGB components.
     * @param r red.
     * @param g green.
     * @param b blue.
     */
    public Color(final int r, final int g, final int b) {
        this((float) r / 255.0f, (float) g / 255.0f, (float) b / 255.0f);
    }

    /**
     * Creates a new color from denormalized (0-255) greyscale component.
     * @param greyscale value.
     */
    public Color(final int greyscale) {
        this((float) greyscale / 255.0f);
    }

    @Override
    public String toString() {
        return String.format("(%d, %d, %d, %d)", (int) (r * 255.0), (int) (g * 255.0), (int) (b * 255.0),
                (int) (a * 255.0));
    }

    /**
     * Red color.
     */
    public static final Color RED = new Color(255, 0, 0);

    /**
     * Green color.
     */
    public static final Color GREEN = new Color(0, 255, 0);

    /**
     * Blue color.
     */
    public static final Color BLUE = new Color(0, 0, 255);

    /**
     * Cyan color.
     */
    public static final Color CYAN = new Color(0, 255, 255);

    /**
     * Yellow color.
     */
    public static final Color YELLOW = new Color(255, 255, 0);

    /**
     * White color.
     */
    public static final Color WHITE = new Color(255, 255, 255);

    /**
     * Black color.
     */
    public static final Color BLACK = new Color(0);
}
