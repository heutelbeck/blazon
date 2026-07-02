/*
 * Copyright (c) 2026 Dominic Heutelbeck (dominic@heutelbeck.com)
 *
 * SPDX-License-Identifier: MIT
 *
 * Licensed under the MIT License. See the LICENSE file in the project root
 * for the full license text.
 */
package com.heutelbeck.blazon;

import java.util.Locale;
import java.util.Map;
import java.util.Optional;

/**
 * Ready-made {@link Palette}s, including the themes used by the banners this
 * library was extracted from.
 */
public final class Palettes {

    /** Teal theme, as used by the SAPL node and playground banners. */
    public static final Palette SAPL = new Palette(Gradient.linear(new Color(123, 189, 188), new Color(2, 131, 146)),
            new Color(62, 160, 167));

    /** Blue-to-sky theme with a coral accent, as used by the Paratron gateway banner. */
    public static final Palette PARATRON = new Palette(Gradient.linear(new Color(28, 52, 170), new Color(96, 165, 240)),
            new Color(224, 108, 117));

    /** Warm amber-to-red theme. */
    public static final Palette EMBER = new Palette(Gradient.linear(new Color(255, 183, 94), new Color(214, 40, 40)),
            new Color(255, 214, 120));

    /** Cool sky-to-deep-blue theme. */
    public static final Palette OCEAN = new Palette(Gradient.linear(new Color(0, 180, 216), new Color(3, 4, 94)),
            new Color(144, 224, 239));

    /** Green mint-to-pine theme. */
    public static final Palette FOREST = new Palette(Gradient.linear(new Color(149, 213, 178), new Color(27, 67, 50)),
            new Color(183, 228, 199));

    /** Three-stop red-orange-gold theme. */
    public static final Palette SUNSET = new Palette(
            Gradient.stops(new Color(255, 94, 77), new Color(255, 159, 67), new Color(255, 206, 84)),
            new Color(255, 230, 167));

    /** Neutral light-to-dark grey theme. */
    public static final Palette MONOCHROME = new Palette(
            Gradient.linear(new Color(230, 230, 230), new Color(90, 90, 90)), new Color(200, 200, 200));

    private static final Map<String, Palette> BY_NAME = Map.of("SAPL", SAPL, "PARATRON", PARATRON, "EMBER", EMBER,
            "OCEAN", OCEAN, "FOREST", FOREST, "SUNSET", SUNSET, "MONOCHROME", MONOCHROME);

    private Palettes() {
    }

    /**
     * Looks up a ready-made palette by name, case-insensitively.
     *
     * @param name the palette name, for example {@code "sapl"}; may be {@code null}
     * @return the matching palette, or empty if the name is unknown or {@code null}
     */
    public static Optional<Palette> byName(String name) {
        if (name == null) {
            return Optional.empty();
        }
        return Optional.ofNullable(BY_NAME.get(name.trim().toUpperCase(Locale.ROOT)));
    }

}
