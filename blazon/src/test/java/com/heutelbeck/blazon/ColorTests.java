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
import static org.junit.jupiter.params.provider.Arguments.arguments;

import java.util.stream.Stream;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

@DisplayName("Color")
class ColorTests {

    @Nested
    @DisplayName("construction")
    class Construction {

        @Test
        @DisplayName("keeps in-range components")
        void whenComponentsInRangeThenComponentsRetained() {
            assertThat(new Color(0, 128, 255)).satisfies(color -> {
                assertThat(color.red()).isZero();
                assertThat(color.green()).isEqualTo(128);
                assertThat(color.blue()).isEqualTo(255);
            });
        }

        @ParameterizedTest(name = "({0},{1},{2}) is rejected")
        @MethodSource("com.heutelbeck.blazon.ColorTests#outOfRangeComponents")
        @DisplayName("rejects out-of-range components")
        void whenAnyComponentOutOfRangeThenThrows(int red, int green, int blue) {
            assertThatExceptionOfType(IllegalArgumentException.class).isThrownBy(() -> new Color(red, green, blue));
        }

    }

    @Nested
    @DisplayName("ANSI rendering")
    class Ansi {

        @Test
        @DisplayName("emits a truecolor foreground escape")
        void whenForegroundAnsiThenTruecolorEscape() {
            assertThat(new Color(10, 20, 30).foregroundAnsi()).isEqualTo("\u001b[38;2;10;20;30m");
        }

    }

    @Nested
    @DisplayName("mixing")
    class Mixing {

        @Test
        @DisplayName("returns the endpoints and the midpoint, and clamps")
        void whenMixedThenBlendedAndClamped() {
            final Color from = new Color(0, 0, 0);
            final Color to   = new Color(100, 200, 40);
            assertThat(Color.mix(from, to, 0.0)).isEqualTo(from);
            assertThat(Color.mix(from, to, 1.0)).isEqualTo(to);
            assertThat(Color.mix(from, to, 0.5)).isEqualTo(new Color(50, 100, 20));
            assertThat(Color.mix(from, to, -1.0)).isEqualTo(from);
            assertThat(Color.mix(from, to, 2.0)).isEqualTo(to);
        }

    }

    static Stream<Arguments> outOfRangeComponents() {
        return Stream.of(arguments(-1, 0, 0), arguments(0, -1, 0), arguments(0, 0, -1), arguments(256, 0, 0),
                arguments(0, 256, 0), arguments(0, 0, 256));
    }

}
