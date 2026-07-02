/*
 * Copyright (c) 2026 Dominic Heutelbeck (dominic@heutelbeck.com)
 *
 * SPDX-License-Identifier: MIT
 *
 * Licensed under the MIT License. See the LICENSE file in the project root
 * for the full license text.
 */
package com.heutelbeck.blazon.spring;

import java.io.PrintStream;
import java.util.Objects;

import org.springframework.boot.Banner;
import org.springframework.core.env.Environment;

import com.heutelbeck.blazon.Blazon;
import com.heutelbeck.blazon.ColorSupport;

/**
 * A Spring Boot {@link Banner} backed by a {@link Blazon}. Metadata lines are
 * resolved against the application {@link Environment} at print time, so
 * placeholders such as {@code ${application.version}} expand to their configured
 * values.
 */
public class BlazonBanner implements Banner {

    private static final String ERROR_NULL_BLAZON = "Blazon must not be null.";

    private final Blazon       blazon;
    private final ColorSupport colorSupport;

    /**
     * Creates a banner that auto-detects colour support at print time.
     *
     * @param blazon the banner description to render
     * @throws NullPointerException if {@code blazon} is {@code null}
     */
    public BlazonBanner(Blazon blazon) {
        this(blazon, null);
    }

    /**
     * Creates a banner with an explicit colour mode.
     *
     * @param blazon the banner description to render
     * @param colorSupport the colour mode, or {@code null} to auto-detect
     * @throws NullPointerException if {@code blazon} is {@code null}
     */
    public BlazonBanner(Blazon blazon, ColorSupport colorSupport) {
        this.blazon       = Objects.requireNonNull(blazon, ERROR_NULL_BLAZON);
        this.colorSupport = colorSupport;
    }

    @Override
    public void printBanner(Environment environment, Class<?> sourceClass, PrintStream out) {
        out.println(blazon.render(resolveColorSupport(), environment::resolvePlaceholders));
    }

    private ColorSupport resolveColorSupport() {
        return colorSupport == null ? ColorSupport.autoDetect() : colorSupport;
    }

}
