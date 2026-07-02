/*
 * Copyright (c) 2026 Dominic Heutelbeck (dominic@heutelbeck.com)
 *
 * SPDX-License-Identifier: MIT
 *
 * Licensed under the MIT License. See the LICENSE file in the project root
 * for the full license text.
 */
package com.heutelbeck.blazon;

/**
 * Factory for the fonts shipped with Blazon.
 */
public final class Fonts {

    private Fonts() {
    }

    /**
     * The house face: a compact two-row half-block font covering the ASCII
     * letters, digits and a small set of punctuation. Letters are rendered
     * upper case; unrepresented characters render as blank space.
     *
     * @return the shared half-block font instance
     */
    public static Font halfBlock() {
        return HalfBlockFont.INSTANCE;
    }

}
