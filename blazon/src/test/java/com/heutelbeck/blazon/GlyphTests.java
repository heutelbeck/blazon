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
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("Glyph")
class GlyphTests {

    @Test
    @DisplayName("exposes its rows and common width")
    void whenCreatedThenRowsAndWidthExposed() {
        assertThat(Glyph.of("ab", "cd")).satisfies(glyph -> {
            assertThat(glyph.rows()).containsExactly("ab", "cd");
            assertThat(glyph.width()).isEqualTo(2);
        });
    }

    @Test
    @DisplayName("rejects rows of differing width")
    void whenRaggedRowsThenThrows() {
        assertThatExceptionOfType(IllegalArgumentException.class).isThrownBy(() -> Glyph.of("ab", "c"));
    }

    @Test
    @DisplayName("rejects an empty glyph")
    void whenNoRowsThenThrows() {
        assertThatExceptionOfType(IllegalArgumentException.class).isThrownBy(Glyph::of);
    }

}
