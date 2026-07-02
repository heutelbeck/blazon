/*
 * Copyright (c) 2026 Dominic Heutelbeck (dominic@heutelbeck.com)
 *
 * SPDX-License-Identifier: MIT
 *
 * Licensed under the MIT License. See the LICENSE file in the project root
 * for the full license text.
 */
package com.heutelbeck.blazon.spring;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Map;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.boot.SpringApplication;
import org.springframework.core.env.MapPropertySource;
import org.springframework.core.env.StandardEnvironment;

import com.heutelbeck.blazon.Blazon;
import com.heutelbeck.blazon.ColorSupport;

@DisplayName("BlazonBannerEnvironmentPostProcessor")
class BlazonBannerEnvironmentPostProcessorTests {

    private final BlazonBannerEnvironmentPostProcessor processor = new BlazonBannerEnvironmentPostProcessor();

    private static StandardEnvironment environmentWith(Map<String, Object> properties) {
        final StandardEnvironment environment = new StandardEnvironment();
        environment.getPropertySources().addFirst(new MapPropertySource("test", properties));
        return environment;
    }

    @Nested
    @DisplayName("banner text")
    class BannerText {

        @Test
        @DisplayName("derives the text from the application name in upper case with dashes as spaces")
        void whenApplicationNameThenHumanized() {
            final StandardEnvironment environment = environmentWith(Map.of("spring.application.name", "blazon-demo"));

            assertThat(BlazonBannerEnvironmentPostProcessor.resolveText(new BlazonProperties(), environment,
                    new SpringApplication())).isEqualTo("BLAZON DEMO");
        }

        @Test
        @DisplayName("prefers an explicitly configured text verbatim")
        void whenExplicitTextThenUsedVerbatim() {
            final BlazonProperties properties = new BlazonProperties();
            properties.setText("Custom Title");
            final StandardEnvironment environment = environmentWith(Map.of("spring.application.name", "blazon-demo"));

            assertThat(
                    BlazonBannerEnvironmentPostProcessor.resolveText(properties, environment, new SpringApplication()))
                    .isEqualTo("Custom Title");
        }

    }

    @Nested
    @DisplayName("banner building")
    class BannerBuilding {

        @Test
        @DisplayName("includes version and OS metadata when available")
        void whenMetadataEnabledThenLinesAppended() {
            final StandardEnvironment environment = environmentWith(
                    Map.of("spring.application.name", "blazon-demo", "spring.application.version", "9.9.9"));

            final Blazon banner   = processor.buildBanner(new BlazonProperties(), environment, new SpringApplication())
                    .orElseThrow();
            final String rendered = banner.render(ColorSupport.NONE, environment::resolvePlaceholders);

            assertThat(rendered).contains("Version: 9.9.9").contains("OS:").contains("█");
        }

        @Test
        @DisplayName("appends the git commit to the version line when available")
        void whenGitCommitPresentThenAppendedToVersion() {
            final StandardEnvironment environment = environmentWith(Map.of("spring.application.name", "blazon-demo",
                    "spring.application.version", "9.9.9", "git.commit.id.abbrev", "abc1234"));

            final Blazon banner   = processor.buildBanner(new BlazonProperties(), environment, new SpringApplication())
                    .orElseThrow();
            final String rendered = banner.render(ColorSupport.NONE, environment::resolvePlaceholders);

            assertThat(rendered).contains("Version: 9.9.9 (abc1234)");
        }

        @Test
        @DisplayName("falls back to the git build version when no application version is set")
        void whenNoApplicationVersionThenGitBuildVersionUsed() {
            final StandardEnvironment environment = environmentWith(Map.of("spring.application.name", "blazon-demo",
                    "git.build.version", "1.2.3", "git.commit.id.abbrev", "def5678"));

            final Blazon banner   = processor.buildBanner(new BlazonProperties(), environment, new SpringApplication())
                    .orElseThrow();
            final String rendered = banner.render(ColorSupport.NONE, environment::resolvePlaceholders);

            assertThat(rendered).contains("Version: 1.2.3 (def5678)");
        }

        @Test
        @DisplayName("omits metadata lines when metadata is disabled")
        void whenMetadataDisabledThenNoLines() {
            final BlazonProperties properties = new BlazonProperties();
            properties.setMetadata(false);
            final StandardEnvironment environment = environmentWith(
                    Map.of("spring.application.name", "blazon-demo", "spring.application.version", "9.9.9"));

            final Blazon banner   = processor.buildBanner(properties, environment, new SpringApplication())
                    .orElseThrow();
            final String rendered = banner.render(ColorSupport.NONE, environment::resolvePlaceholders);

            assertThat(rendered).doesNotContain("Version:").doesNotContain("OS:");
        }

    }

}
