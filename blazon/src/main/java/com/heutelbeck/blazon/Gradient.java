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
 * Maps a blend position in {@code 0..1} to a {@link Color}. Implementations
 * should tolerate values slightly outside that range by clamping.
 */
@FunctionalInterface
public interface Gradient {

    /** Message for the empty-palette guard in {@link #stops(Color...)}. */
    String ERROR_NO_COLORS = "A gradient needs at least one colour.";

    /**
     * Samples the gradient.
     *
     * @param t the blend position, expected in {@code 0..1}
     * @return the colour at that position
     */
    Color at(double t);

    /**
     * A gradient that returns the same colour everywhere.
     *
     * @param color the constant colour
     * @return the solid gradient
     */
    static Gradient solid(Color color) {
        return t -> color;
    }

    /**
     * A two-stop linear gradient.
     *
     * @param from the colour at {@code t == 0}
     * @param to the colour at {@code t == 1}
     * @return the linear gradient
     */
    static Gradient linear(Color from, Color to) {
        return t -> Color.mix(from, to, t);
    }

    /**
     * A multi-stop linear gradient with the stops spread evenly across
     * {@code 0..1}.
     *
     * @param colors the ordered colour stops; at least one is required
     * @return the multi-stop gradient
     * @throws IllegalArgumentException if no colours are given
     */
    static Gradient stops(Color... colors) {
        if (colors.length == 0) {
            throw new IllegalArgumentException(ERROR_NO_COLORS);
        }
        final Color[] palette = colors.clone();
        if (palette.length == 1) {
            return solid(palette[0]);
        }
        return t -> {
            final double clamped = Math.max(0.0, Math.min(1.0, t));
            final double scaled  = clamped * (palette.length - 1);
            final int    lower   = Math.min((int) Math.floor(scaled), palette.length - 2);
            return Color.mix(palette[lower], palette[lower + 1], scaled - lower);
        };
    }

}
