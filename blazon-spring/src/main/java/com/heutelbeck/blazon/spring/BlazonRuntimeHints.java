/*
 * Copyright (c) 2026 Dominic Heutelbeck (dominic@heutelbeck.com)
 *
 * SPDX-License-Identifier: MIT
 *
 * Licensed under the MIT License. See the LICENSE file in the project root
 * for the full license text.
 */
package com.heutelbeck.blazon.spring;

import org.springframework.aot.hint.BindingReflectionHintsRegistrar;
import org.springframework.aot.hint.RuntimeHints;
import org.springframework.aot.hint.RuntimeHintsRegistrar;

/**
 * Registers native-image reflection metadata for {@link BlazonProperties}. The banner's
 * {@code EnvironmentPostProcessor} binds those properties with the reflective {@code Binder} before
 * the application context exists, and the type is not a context-registered {@code @ConfigurationProperties}
 * bean, so ahead-of-time processing does not otherwise discover it. Without this the bind falls back
 * to defaults in a native image and configured values such as the palette are lost.
 */
public class BlazonRuntimeHints implements RuntimeHintsRegistrar {

    /**
     * Creates the registrar. Instantiated by Spring's ahead-of-time processing.
     */
    public BlazonRuntimeHints() {
    }

    @Override
    public void registerHints(RuntimeHints hints, ClassLoader classLoader) {
        new BindingReflectionHintsRegistrar().registerReflectionHints(hints.reflection(), BlazonProperties.class);
    }
}
