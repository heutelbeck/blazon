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
 * A block font: a mapping from characters to fixed-height {@link Glyph}s. All
 * glyphs of a font share the same {@link #height()}; widths may vary per
 * character. Implementations decide how to handle unknown characters (for
 * example by returning a blank glyph).
 */
public interface Font {

    /**
     * The number of rows every glyph of this font occupies.
     *
     * @return the glyph height in rows
     */
    int height();

    /**
     * Returns the glyph for a character, or a fallback glyph if the character is
     * not represented.
     *
     * @param character the character to render
     * @return a glyph with {@link #height()} rows
     */
    Glyph glyph(char character);

}
