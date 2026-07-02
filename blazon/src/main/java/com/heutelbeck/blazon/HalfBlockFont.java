/*
 * Copyright (c) 2026 Dominic Heutelbeck (dominic@heutelbeck.com)
 *
 * SPDX-License-Identifier: MIT
 *
 * Licensed under the MIT License. See the LICENSE file in the project root
 * for the full license text.
 */
package com.heutelbeck.blazon;

import java.util.HashMap;
import java.util.Map;

/**
 * The house face: a two-row font built from the half-block glyphs
 * {@code U+2588 (full block)}, {@code U+2580 (upper half)} and
 * {@code U+2584 (lower half)}. The glyph shapes below are font data, hence the
 * non-ASCII content.
 */
final class HalfBlockFont implements Font {

    static final HalfBlockFont INSTANCE = new HalfBlockFont();

    private static final int   HEIGHT   = 2;
    private static final Glyph FALLBACK = Glyph.of("  ", "  ");

    private static final Map<Character, Glyph> GLYPHS = buildGlyphs();

    private HalfBlockFont() {
    }

    @Override
    public int height() {
        return HEIGHT;
    }

    @Override
    public Glyph glyph(char character) {
        return GLYPHS.getOrDefault(Character.toUpperCase(character), FALLBACK);
    }

    private static Map<Character, Glyph> buildGlyphs() {
        final Map<Character, Glyph> glyphs = new HashMap<>();
        glyphs.put(' ', Glyph.of("  ", "  "));
        glyphs.put('A', Glyph.of("▄▀█", "█▀█"));
        glyphs.put('B', Glyph.of("██▄", "█▄█"));
        glyphs.put('C', Glyph.of("█▀▀", "█▄▄"));
        glyphs.put('D', Glyph.of("█▀▄", "█▄▀"));
        glyphs.put('E', Glyph.of("█▀▀", "██▄"));
        glyphs.put('F', Glyph.of("█▀▀", "█▀ "));
        glyphs.put('G', Glyph.of("█▀▀", "█▄█"));
        glyphs.put('H', Glyph.of("█ █", "█▀█"));
        glyphs.put('I', Glyph.of("█", "█"));
        glyphs.put('J', Glyph.of(" █", "▄█"));
        glyphs.put('K', Glyph.of("█▄▀", "█▀▄"));
        glyphs.put('L', Glyph.of("█  ", "█▄▄"));
        glyphs.put('M', Glyph.of("█▀▄▀█", "█ ▀ █"));
        glyphs.put('N', Glyph.of("█▄ █", "█ ▀█"));
        glyphs.put('O', Glyph.of("█▀█", "█▄█"));
        glyphs.put('P', Glyph.of("█▀█", "█▀▀"));
        glyphs.put('Q', Glyph.of("█▀█", "▀▀█"));
        glyphs.put('R', Glyph.of("█▀█", "█▀▄"));
        glyphs.put('S', Glyph.of("█▀", "▄█"));
        glyphs.put('T', Glyph.of("▀█▀", " █ "));
        glyphs.put('U', Glyph.of("█ █", "█▄█"));
        glyphs.put('V', Glyph.of("█ █", "▀▄▀"));
        glyphs.put('W', Glyph.of("█   █", "█▄▀▄█"));
        glyphs.put('X', Glyph.of("▀▄▀", "▄▀▄"));
        glyphs.put('Y', Glyph.of("█ █", " █ "));
        glyphs.put('Z', Glyph.of("▀▀█", "█▄▄"));
        glyphs.put('0', Glyph.of("█▀█", "█▄█"));
        glyphs.put('1', Glyph.of("▄█", " █"));
        glyphs.put('2', Glyph.of("▀▀█", "█▄▄"));
        glyphs.put('3', Glyph.of("▀▀█", "▄██"));
        glyphs.put('4', Glyph.of("█▄█", "  █"));
        glyphs.put('5', Glyph.of("█▀▀", "▄▄█"));
        glyphs.put('6', Glyph.of("█▄▄", "█▄█"));
        glyphs.put('7', Glyph.of("▀▀█", "  █"));
        glyphs.put('8', Glyph.of("█▄█", "█▄█"));
        glyphs.put('9', Glyph.of("█▄█", "▄▄█"));
        glyphs.put('.', Glyph.of(" ", "▄"));
        glyphs.put(':', Glyph.of("▀", "▄"));
        glyphs.put('-', Glyph.of("▄▄", "  "));
        glyphs.put('!', Glyph.of("█", "▄"));
        return Map.copyOf(glyphs);
    }

}
