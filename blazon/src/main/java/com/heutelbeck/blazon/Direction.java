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
 * The axis along which a {@link Gradient} is sampled across the rendered glyph
 * grid.
 */
public enum Direction {

    /** Left to right: colour depends on the column. */
    HORIZONTAL,

    /** Top to bottom: colour depends on the row. */
    VERTICAL,

    /** Top-left to bottom-right: colour depends on column plus row. */
    DIAGONAL;

    /**
     * Maps a cell position to a gradient blend value in {@code 0..1}.
     *
     * @param x the zero-based column
     * @param y the zero-based row
     * @param width the total number of columns
     * @param height the total number of rows
     * @return the blend position for the cell
     */
    double position(int x, int y, int width, int height) {
        return switch (this) {
        case HORIZONTAL -> fraction(x, width);
        case VERTICAL   -> fraction(y, height);
        case DIAGONAL   -> {
            final int span = (width - 1) + (height - 1);
            yield span <= 0 ? 0.0 : (double) (x + y) / span;
        }
        };
    }

    private static double fraction(int index, int size) {
        return size <= 1 ? 0.0 : (double) index / (size - 1);
    }

}
