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

@DisplayName("Blazon")
class BlazonTests {

    private static final String ESC = "\u001b";

    @Nested
    @DisplayName("rendering")
    class Rendering {

        @Test
        @DisplayName("without colour emits plain glyph art and no escapes")
        void whenColorNoneThenPlainArt() {
            final String rendered = Blazon.of("A").render(ColorSupport.NONE);
            assertThat(rendered).contains("▄▀█").contains("█▀█").doesNotContain(ESC);
        }

        @Test
        @DisplayName("with truecolor wraps ink cells in colour escapes and a reset")
        void whenColorTruecolorThenEscapes() {
            final String rendered = Blazon.of("A").gradient(Gradient.solid(new Color(1, 2, 3)))
                    .render(ColorSupport.TRUECOLOR);
            assertThat(rendered).contains(ESC + "[38;2;1;2;3m").contains(ESC + "[0m");
        }

        @Test
        @DisplayName("appends metadata lines and applies the line resolver")
        void whenLinesWithResolverThenResolvedAndAppended() {
            final String rendered = Blazon.of("A").line("v=${x}").render(ColorSupport.NONE,
                    text -> text.replace("${x}", "1"));
            assertThat(rendered).contains("v=1").doesNotContain("${x}");
        }

    }

    @Nested
    @DisplayName("configuration")
    class Configuration {

        @Test
        @DisplayName("wider letter spacing produces wider output and leaves the source unchanged")
        void whenLetterSpacingIncreasedThenWiderAndImmutable() {
            final Blazon base    = Blazon.of("AB");
            final Blazon spaced  = base.letterSpacing(5);
            final String baseOut = base.render(ColorSupport.NONE);

            assertThat(spaced.render(ColorSupport.NONE).length()).isGreaterThan(baseOut.length());
            assertThat(baseOut).isEqualTo(Blazon.of("AB").render(ColorSupport.NONE));
        }

        @Test
        @DisplayName("applying a palette is equivalent to setting its gradient and accent")
        void whenPaletteThenGradientAndAccentApplied() {
            final Palette palette    = Palettes.SAPL;
            final String  viaPalette = Blazon.of("A").palette(palette).line("x").render(ColorSupport.TRUECOLOR);
            final String  viaParts   = Blazon.of("A").gradient(palette.gradient()).accent(palette.accent()).line("x")
                    .render(ColorSupport.TRUECOLOR);

            assertThat(viaPalette).isEqualTo(viaParts);
        }

        @Test
        @DisplayName("rejects negative letter spacing")
        void whenNegativeLetterSpacingThenThrows() {
            assertThatExceptionOfType(IllegalArgumentException.class)
                    .isThrownBy(() -> Blazon.of("A").letterSpacing(-1));
        }

        @Test
        @DisplayName("rejects null text")
        void whenNullTextThenThrows() {
            assertThatExceptionOfType(NullPointerException.class).isThrownBy(() -> Blazon.of(null));
        }

    }

    @Nested
    @DisplayName("compatibility with the original banners")
    class Compatibility {

        @Test
        @DisplayName("reproduces the SAPL node banner glyph art exactly with tight word spacing")
        void whenSaplNodeWithMarginThenMatchesReferenceArt() {
            final String expected = " █▀ ▄▀█ █▀█ █   █▄ █ █▀█ █▀▄ █▀▀\n ▄█ █▀█ █▀▀ █▄▄ █ ▀█ █▄█ █▄▀ ██▄";

            assertThat(Blazon.of("SAPL NODE").margin(1).wordSpacing(0).render(ColorSupport.NONE)).isEqualTo(expected);
        }

    }

    @Nested
    @DisplayName("word spacing")
    class WordSpacing {

        @Test
        @DisplayName("a space adds padding beyond the plain letter gap")
        void whenSpaceThenWiderThanNoSpace() {
            final int joined  = Blazon.of("AA").render(ColorSupport.NONE).indexOf('\n');
            final int spaced  = Blazon.of("A A").render(ColorSupport.NONE).indexOf('\n');
            final int tighter = Blazon.of("A A").wordSpacing(0).render(ColorSupport.NONE).indexOf('\n');

            assertThat(spaced).isGreaterThan(joined);
            assertThat(tighter).isEqualTo(joined);
        }

    }

}
