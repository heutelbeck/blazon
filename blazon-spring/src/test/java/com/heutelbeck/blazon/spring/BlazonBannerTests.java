/*
 * Copyright (c) 2026 Dominic Heutelbeck (dominic@heutelbeck.com)
 *
 * SPDX-License-Identifier: MIT
 *
 * Licensed under the MIT License. See the LICENSE file in the project root
 * for the full license text.
 */
package com.heutelbeck.blazon.spring;

import static java.nio.charset.StandardCharsets.UTF_8;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.Map;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.core.env.MapPropertySource;
import org.springframework.core.env.StandardEnvironment;

import com.heutelbeck.blazon.Blazon;
import com.heutelbeck.blazon.ColorSupport;

@DisplayName("BlazonBanner")
class BlazonBannerTests {

    @Test
    @DisplayName("resolves environment placeholders and prints the glyph art")
    void whenPrintedThenPlaceholdersResolvedAndArtRendered() {
        final StandardEnvironment environment = new StandardEnvironment();
        environment.getPropertySources()
                .addFirst(new MapPropertySource("test", Map.of("application.version", "1.2.3")));
        final Blazon                blazon = Blazon.of("APP").line("Version: ${application.version}");
        final ByteArrayOutputStream sink   = new ByteArrayOutputStream();

        new BlazonBanner(blazon, ColorSupport.NONE).printBanner(environment, BlazonBannerTests.class,
                new PrintStream(sink, true, UTF_8));

        assertThat(sink.toString(UTF_8)).contains("Version: 1.2.3").contains("█").doesNotContain("${");
    }

    @Test
    @DisplayName("rejects a null banner")
    void whenNullBlazonThenThrows() {
        assertThatExceptionOfType(NullPointerException.class).isThrownBy(() -> new BlazonBanner(null));
    }

}
