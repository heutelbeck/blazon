/*
 * Copyright (c) 2026 Dominic Heutelbeck (dominic@heutelbeck.com)
 *
 * SPDX-License-Identifier: MIT
 *
 * Licensed under the MIT License. See the LICENSE file in the project root
 * for the full license text.
 */
package com.heutelbeck.blazon;

/**
 * An immutable 24-bit RGB colour.
 *
 * @param red the red component, {@code 0..255}
 * @param green the green component, {@code 0..255}
 * @param blue the blue component, {@code 0..255}
 */
public record Color(int red, int green, int blue) {

    private static final String ERROR_COMPONENT_OUT_OF_RANGE = "Colour components must be in the range 0..255.";

    /**
     * Validates that every component is within the representable range.
     *
     * @throws IllegalArgumentException if any component is outside {@code 0..255}
     */
    public Color {
        if (outOfRange(red) || outOfRange(green) || outOfRange(blue)) {
            throw new IllegalArgumentException(ERROR_COMPONENT_OUT_OF_RANGE);
        }
    }

    /**
     * Returns the ANSI truecolor escape that sets this colour as the foreground.
     *
     * @return the {@code ESC[38;2;r;g;bm} control sequence
     */
    public String foregroundAnsi() {
        return "\u001b[38;2;" + red + ";" + green + ";" + blue + "m";
    }

    /**
     * Linearly interpolates between two colours.
     *
     * @param from the colour returned at {@code t == 0}
     * @param to the colour returned at {@code t == 1}
     * @param t the blend position; values outside {@code 0..1} are clamped
     * @return the interpolated colour
     */
    public static Color mix(Color from, Color to, double t) {
        final double clamped = Math.max(0.0, Math.min(1.0, t));
        return new Color(component(from.red, to.red, clamped), component(from.green, to.green, clamped),
                component(from.blue, to.blue, clamped));
    }

    private static int component(int from, int to, double t) {
        return (int) Math.round(from + (to - from) * t);
    }

    private static boolean outOfRange(int component) {
        return component < 0 || component > 255;
    }

}
