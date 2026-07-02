/*
 * Copyright (c) 2026 Dominic Heutelbeck (dominic@heutelbeck.com)
 *
 * SPDX-License-Identifier: MIT
 *
 * Licensed under the MIT License. See the LICENSE file in the project root
 * for the full license text.
 */
package com.heutelbeck.blazon;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("HalfBlockFont")
class HalfBlockFontTests {

    private static final String SUPPORTED = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789 .:-!";

    private final Font font = Fonts.halfBlock();

    @Test
    @DisplayName("is two rows tall")
    void whenHeightThenTwo() {
        assertThat(font.height()).isEqualTo(2);
    }

    @Test
    @DisplayName("maps lower case to the same glyph as upper case")
    void whenLowerCaseThenSameAsUpperCase() {
        assertThat(font.glyph('a')).isEqualTo(font.glyph('A'));
    }

    @Test
    @DisplayName("renders unknown characters as blank space")
    void whenUnknownCharacterThenBlankFallback() {
        assertThat(font.glyph('~').rows()).containsExactly("  ", "  ");
    }

    @Test
    @DisplayName("every supported glyph has height rows of a single common width")
    void whenSupportedGlyphThenWellFormed() {
        for (final char character : SUPPORTED.toCharArray()) {
            final Glyph glyph = font.glyph(character);
            assertThat(glyph.rows()).as("glyph '%s' rows", character).hasSize(font.height())
                    .allMatch(row -> row.length() == glyph.width());
        }
    }

}
