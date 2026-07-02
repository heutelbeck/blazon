/*
 * Copyright (c) 2026 Dominic Heutelbeck (dominic@heutelbeck.com)
 *
 * SPDX-License-Identifier: MIT
 *
 * Licensed under the MIT License. See the LICENSE file in the project root
 * for the full license text.
 */
package com.heutelbeck.blazon;

import java.util.Objects;

/**
 * A named colour theme: the {@link Gradient} that paints the glyph art together
 * with the accent {@link Color} used for the metadata lines. Ready-made palettes
 * are available in {@link Palettes}.
 *
 * @param gradient the gradient for the glyph art
 * @param accent the colour for the metadata lines
 */
public record Palette(Gradient gradient, Color accent) {

    private static final String ERROR_NULL_ACCENT = "Palette accent must not be null.";
    private static final String ERROR_NULL_GRADIENT = "Palette gradient must not be null.";

    /**
     * Validates the components.
     *
     * @param gradient the gradient for the glyph art
     * @param accent the colour for the metadata lines
     * @throws NullPointerException if the gradient or accent is {@code null}
     */
    public Palette {
        Objects.requireNonNull(gradient, ERROR_NULL_GRADIENT);
        Objects.requireNonNull(accent, ERROR_NULL_ACCENT);
    }

}
