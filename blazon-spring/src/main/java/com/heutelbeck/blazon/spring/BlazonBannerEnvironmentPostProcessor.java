/*
 * Copyright (c) 2026 Dominic Heutelbeck (dominic@heutelbeck.com)
 *
 * SPDX-License-Identifier: MIT
 *
 * Licensed under the MIT License. See the LICENSE file in the project root
 * for the full license text.
 */
package com.heutelbeck.blazon.spring;

import java.util.Locale;
import java.util.Optional;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.context.properties.bind.Binder;
import org.springframework.boot.env.EnvironmentPostProcessor;
import org.springframework.core.Ordered;
import org.springframework.core.env.ConfigurableEnvironment;

import com.heutelbeck.blazon.Blazon;
import com.heutelbeck.blazon.ColorSupport;
import com.heutelbeck.blazon.Palette;
import com.heutelbeck.blazon.Palettes;

/**
 * Installs a {@link BlazonBanner} as the startup banner without any user code:
 * merely depending on this module replaces the default Spring Boot banner.
 *
 * <p>
 * This runs while the environment is being prepared, after configuration data
 * has been loaded and before the banner is printed, so it can read
 * {@code spring.application.name} and call
 * {@link SpringApplication#setBanner(org.springframework.boot.Banner)}. If the
 * application defines a {@code banner.txt}, Spring Boot's precedence keeps it;
 * set {@code blazon.enabled=false} to opt out entirely.
 */
public class BlazonBannerEnvironmentPostProcessor implements EnvironmentPostProcessor, Ordered {

    private static final String APPLICATION_NAME_PROPERTY    = "spring.application.name";
    private static final String APPLICATION_VERSION_PROPERTY = "spring.application.version";
    private static final String CLASS_NAME_SUFFIX            = "Application";
    private static final String GIT_COMMIT_PROPERTY          = "git.commit.id.abbrev";
    private static final String GIT_VERSION_PROPERTY         = "git.build.version";
    private static final String OS_LINE                      = "OS:      ${os.name} ${os.version} ${os.arch}";

    @Override
    public int getOrder() {
        return Ordered.LOWEST_PRECEDENCE;
    }

    @Override
    public void postProcessEnvironment(ConfigurableEnvironment environment, SpringApplication application) {
        final BlazonProperties properties = Binder.get(environment).bind("blazon", BlazonProperties.class)
                .orElseGet(BlazonProperties::new);
        if (!properties.isEnabled()) {
            return;
        }
        buildBanner(properties, environment, application).ifPresent(
                blazon -> application.setBanner(new BlazonBanner(blazon, parseColor(properties.getColor()))));
    }

    Optional<Blazon> buildBanner(BlazonProperties properties, ConfigurableEnvironment environment,
            SpringApplication application) {
        final String text = resolveText(properties, environment, application);
        if (text == null || text.isBlank()) {
            return Optional.empty();
        }
        final Palette palette = Palettes.byName(properties.getPalette()).orElse(Palettes.EMBER);
        Blazon        blazon  = Blazon.of(text).palette(palette).margin(properties.getMargin());
        if (properties.isMetadata()) {
            final String versionLine = versionLine(environment);
            if (versionLine != null) {
                blazon = blazon.line(versionLine);
            }
            blazon = blazon.line(OS_LINE);
        }
        return Optional.of(blazon);
    }

    /**
     * Builds a {@code Version: ...} line matching the original banners, with the
     * git commit appended when available, or {@code null} if no version property
     * is present. The values are left as placeholders and resolved at print time.
     */
    private static String versionLine(ConfigurableEnvironment environment) {
        final String versionKey = versionKey(environment);
        if (versionKey == null) {
            return null;
        }
        final StringBuilder line = new StringBuilder("Version: ${").append(versionKey).append('}');
        if (environment.containsProperty(GIT_COMMIT_PROPERTY)) {
            line.append(" (${").append(GIT_COMMIT_PROPERTY).append("})");
        }
        return line.toString();
    }

    private static String versionKey(ConfigurableEnvironment environment) {
        if (environment.containsProperty(APPLICATION_VERSION_PROPERTY)) {
            return APPLICATION_VERSION_PROPERTY;
        }
        if (environment.containsProperty(GIT_VERSION_PROPERTY)) {
            return GIT_VERSION_PROPERTY;
        }
        return null;
    }

    static String resolveText(BlazonProperties properties, ConfigurableEnvironment environment,
            SpringApplication application) {
        if (hasText(properties.getText())) {
            return properties.getText();
        }
        final String applicationName = environment.getProperty(APPLICATION_NAME_PROPERTY);
        if (hasText(applicationName)) {
            return humanize(applicationName);
        }
        final String fromClass = mainClassName(application);
        return hasText(fromClass) ? humanize(fromClass) : null;
    }

    private static String mainClassName(SpringApplication application) {
        final Class<?> mainClass = application.getMainApplicationClass();
        if (mainClass == null) {
            return null;
        }
        final String simpleName = mainClass.getSimpleName();
        return simpleName.endsWith(CLASS_NAME_SUFFIX)
                ? simpleName.substring(0, simpleName.length() - CLASS_NAME_SUFFIX.length())
                : simpleName;
    }

    private static String humanize(String raw) {
        final String spaced = raw.replaceAll("([a-z0-9])([A-Z])", "$1 $2").replace('-', ' ').replace('_', ' ');
        return spaced.trim().replaceAll("\\s+", " ").toUpperCase(Locale.ROOT);
    }

    private static ColorSupport parseColor(String color) {
        if (!hasText(color)) {
            return null;
        }
        return switch (color.trim().toUpperCase(Locale.ROOT)) {
        case "TRUECOLOR" -> ColorSupport.TRUECOLOR;
        case "NONE"      -> ColorSupport.NONE;
        default          -> null;
        };
    }

    private static boolean hasText(String value) {
        return value != null && !value.isBlank();
    }

}
