/*
 * Copyright (c) 2026 Dominic Heutelbeck (dominic@heutelbeck.com)
 *
 * SPDX-License-Identifier: MIT
 *
 * Licensed under the MIT License. See the LICENSE file in the project root
 * for the full license text.
 */
package com.heutelbeck.blazon;

import java.util.List;

/**
 * The rendered shape of a single character: a fixed number of equal-width rows.
 * All rows share the same width so that glyphs align when concatenated.
 *
 * @param rows the character rows, top to bottom; every row has the same length
 */
public record Glyph(List<String> rows) {

    private static final String ERROR_EMPTY = "A glyph must have at least one row.";
    private static final String ERROR_RAGGED = "All rows of a glyph must have the same width.";

    /**
     * Validates and defensively copies the rows.
     *
     * @throws IllegalArgumentException if there are no rows or the rows differ in
     * width
     */
    public Glyph {
        if (rows.isEmpty()) {
            throw new IllegalArgumentException(ERROR_EMPTY);
        }
        final int width = rows.get(0).length();
        for (final String row : rows) {
            if (row.length() != width) {
                throw new IllegalArgumentException(ERROR_RAGGED);
            }
        }
        rows = List.copyOf(rows);
    }

    /**
     * Convenience factory taking the rows as varargs.
     *
     * @param rows the character rows, top to bottom
     * @return the glyph
     */
    public static Glyph of(String... rows) {
        return new Glyph(List.of(rows));
    }

    /**
     * Returns the common width of every row.
     *
     * @return the glyph width in cells
     */
    public int width() {
        return rows.get(0).length();
    }

}
