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
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

@DisplayName("Gradient")
class GradientTests {

    private static final Color BLACK = new Color(0, 0, 0);
    private static final Color WHITE = new Color(255, 255, 255);
    private static final Color GREY  = new Color(128, 128, 128);

    @Nested
    @DisplayName("solid")
    class Solid {

        @Test
        @DisplayName("returns the constant colour everywhere")
        void whenSampledThenAlwaysConstant() {
            final Gradient gradient = Gradient.solid(GREY);
            assertThat(gradient.at(0.0)).isEqualTo(GREY);
            assertThat(gradient.at(0.5)).isEqualTo(GREY);
            assertThat(gradient.at(1.0)).isEqualTo(GREY);
        }

    }

    @Nested
    @DisplayName("linear")
    class Linear {

        @Test
        @DisplayName("interpolates between the two endpoints")
        void whenSampledThenInterpolated() {
            final Gradient gradient = Gradient.linear(BLACK, WHITE);
            assertThat(gradient.at(0.0)).isEqualTo(BLACK);
            assertThat(gradient.at(1.0)).isEqualTo(WHITE);
            assertThat(gradient.at(0.5)).isEqualTo(new Color(128, 128, 128));
        }

    }

    @Nested
    @DisplayName("stops")
    class Stops {

        @Test
        @DisplayName("degenerates to solid for a single stop")
        void whenSingleStopThenSolid() {
            final Gradient gradient = Gradient.stops(GREY);
            assertThat(gradient.at(0.0)).isEqualTo(GREY);
            assertThat(gradient.at(1.0)).isEqualTo(GREY);
        }

        @Test
        @DisplayName("spreads multiple stops evenly and clamps")
        void whenMultipleStopsThenEvenlySpread() {
            final Gradient gradient = Gradient.stops(BLACK, WHITE, BLACK);
            assertThat(gradient.at(0.0)).isEqualTo(BLACK);
            assertThat(gradient.at(0.5)).isEqualTo(WHITE);
            assertThat(gradient.at(1.0)).isEqualTo(BLACK);
            assertThat(gradient.at(2.0)).isEqualTo(BLACK);
        }

        @Test
        @DisplayName("rejects an empty palette")
        void whenNoStopsThenThrows() {
            assertThatExceptionOfType(IllegalArgumentException.class).isThrownBy(Gradient::stops);
        }

    }

}
