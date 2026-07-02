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
 * Whether a rendering target can display ANSI truecolor.
 */
public enum ColorSupport {

    /** Emit 24-bit ANSI colour escapes. */
    TRUECOLOR,

    /** Emit plain text with no colour escapes. */
    NONE;

    /**
     * Best-effort detection of colour support, following the conventions used by
     * the reference banners: {@code NO_COLOR} disables colour, {@code FORCE_COLOR}
     * forces it, otherwise colour is enabled only when a console is attached.
     *
     * @return {@link #NONE} if colour is disabled or output is not interactive,
     * otherwise {@link #TRUECOLOR}
     */
    public static ColorSupport autoDetect() {
        if (System.getenv("NO_COLOR") != null) {
            return NONE;
        }
        if (System.getenv("FORCE_COLOR") != null) {
            return TRUECOLOR;
        }
        return System.console() != null ? TRUECOLOR : NONE;
    }

}
