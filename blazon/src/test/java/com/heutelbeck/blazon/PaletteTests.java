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

@DisplayName("Palette")
class PaletteTests {

    @Nested
    @DisplayName("construction")
    class Construction {

        @Test
        @DisplayName("exposes its gradient and accent")
        void whenCreatedThenComponentsExposed() {
            final Gradient gradient = Gradient.solid(new Color(1, 2, 3));
            final Color    accent   = new Color(4, 5, 6);
            assertThat(new Palette(gradient, accent)).satisfies(palette -> {
                assertThat(palette.gradient()).isSameAs(gradient);
                assertThat(palette.accent()).isEqualTo(accent);
            });
        }

        @Test
        @DisplayName("rejects a null gradient")
        void whenNullGradientThenThrows() {
            assertThatExceptionOfType(NullPointerException.class)
                    .isThrownBy(() -> new Palette(null, new Color(0, 0, 0)));
        }

        @Test
        @DisplayName("rejects a null accent")
        void whenNullAccentThenThrows() {
            final Gradient gradient = Gradient.solid(new Color(0, 0, 0));
            assertThatExceptionOfType(NullPointerException.class).isThrownBy(() -> new Palette(gradient, null));
        }

    }

    @Nested
    @DisplayName("ready-made palettes")
    class ReadyMade {

        @ParameterizedTest(name = "{0}")
        @MethodSource("com.heutelbeck.blazon.PaletteTests#readyMadePalettes")
        @DisplayName("is fully defined and samples to valid colours")
        void whenSampledThenWellDefined(String name, Palette palette) {
            assertThat(palette.accent()).isNotNull();
            assertThat(palette.gradient()).isNotNull();
            assertThat(palette.gradient().at(0.0)).isNotNull();
            assertThat(palette.gradient().at(0.5)).isNotNull();
            assertThat(palette.gradient().at(1.0)).isNotNull();
        }

    }

    @Nested
    @DisplayName("lookup by name")
    class Lookup {

        @Test
        @DisplayName("resolves a known name case-insensitively")
        void whenKnownNameThenResolved() {
            assertThat(Palettes.byName("sapl")).contains(Palettes.SAPL);
            assertThat(Palettes.byName(" PARATRON ")).contains(Palettes.PARATRON);
        }

        @Test
        @DisplayName("returns empty for unknown or null names")
        void whenUnknownOrNullThenEmpty() {
            assertThat(Palettes.byName("nope")).isEmpty();
            assertThat(Palettes.byName(null)).isEmpty();
        }

    }

    static Stream<Arguments> readyMadePalettes() {
        return Stream.of(arguments("SAPL", Palettes.SAPL), arguments("PARATRON", Palettes.PARATRON),
                arguments("EMBER", Palettes.EMBER), arguments("OCEAN", Palettes.OCEAN),
                arguments("FOREST", Palettes.FOREST), arguments("SUNSET", Palettes.SUNSET),
                arguments("MONOCHROME", Palettes.MONOCHROME));
    }

}
