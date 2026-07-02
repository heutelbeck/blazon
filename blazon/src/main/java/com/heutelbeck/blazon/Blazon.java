/*
 * Copyright (c) 2026 Dominic Heutelbeck (dominic@heutelbeck.com)
 *
 * SPDX-License-Identifier: MIT
 *
 * Licensed under the MIT License. See the LICENSE file in the project root
 * for the full license text.
 */
package com.heutelbeck.blazon;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.UnaryOperator;

/**
 * An immutable description of a banner and the entry point for building one. The
 * fluent methods each return a new instance, so a {@code Blazon} can be shared
 * and specialised safely.
 *
 * <p>
 * A banner is a line of glyph art painted with a {@link Gradient} along a
 * {@link Direction}, followed by zero or more metadata lines painted in a single
 * accent {@link Color}.
 */
public final class Blazon {

    private static final String ANSI_RESET = "\u001b[0m";

    private static final Color DEFAULT_COLOR = new Color(255, 255, 255);

    private static final String ERROR_NEGATIVE_MARGIN  = "Margin must not be negative.";
    private static final String ERROR_NEGATIVE_SPACING = "Spacing must not be negative.";
    private static final String ERROR_NULL_TEXT        = "Banner text must not be null.";

    private final String       text;
    private final Font         font;
    private final Gradient     gradient;
    private final Direction    direction;
    private final Color        accent;
    private final List<String> lines;
    private final int          letterSpacing;
    private final int          wordSpacing;
    private final int          margin;

    private Blazon(String text,
            Font font,
            Gradient gradient,
            Direction direction,
            Color accent,
            List<String> lines,
            int letterSpacing,
            int wordSpacing,
            int margin) {
        this.text          = text;
        this.font          = font;
        this.gradient      = gradient;
        this.direction     = direction;
        this.accent        = accent;
        this.lines         = lines;
        this.letterSpacing = letterSpacing;
        this.wordSpacing   = wordSpacing;
        this.margin        = margin;
    }

    /**
     * Starts a banner for the given text with sensible defaults: the half-block
     * font, a solid white gradient and accent, horizontal direction, a one-cell
     * gap between glyphs, two extra cells per space, and no left margin.
     *
     * @param text the text to render as glyph art; spaces separate words
     * @return a new banner description
     * @throws NullPointerException if {@code text} is {@code null}
     */
    public static Blazon of(String text) {
        Objects.requireNonNull(text, ERROR_NULL_TEXT);
        return new Blazon(text, Fonts.halfBlock(), Gradient.solid(DEFAULT_COLOR), Direction.HORIZONTAL, DEFAULT_COLOR,
                List.of(), 1, 2, 0);
    }

    /**
     * Returns a copy using the given font for the glyph art.
     *
     * @param font the font to render with
     * @return the derived banner
     */
    public Blazon font(Font font) {
        return new Blazon(text, font, gradient, direction, accent, lines, letterSpacing, wordSpacing, margin);
    }

    /**
     * Returns a copy using the given gradient for the glyph art.
     *
     * @param gradient the gradient to paint the art with
     * @return the derived banner
     */
    public Blazon gradient(Gradient gradient) {
        return new Blazon(text, font, gradient, direction, accent, lines, letterSpacing, wordSpacing, margin);
    }

    /**
     * Returns a copy sampling the gradient along the given direction.
     *
     * @param direction the gradient axis
     * @return the derived banner
     */
    public Blazon direction(Direction direction) {
        return new Blazon(text, font, gradient, direction, accent, lines, letterSpacing, wordSpacing, margin);
    }

    /**
     * Returns a copy painting the metadata lines in the given accent colour.
     *
     * @param accent the metadata colour
     * @return the derived banner
     */
    public Blazon accent(Color accent) {
        return new Blazon(text, font, gradient, direction, accent, lines, letterSpacing, wordSpacing, margin);
    }

    /**
     * Returns a copy adopting a palette's gradient and accent colour together.
     *
     * @param palette the colour theme to apply
     * @return the derived banner
     */
    public Blazon palette(Palette palette) {
        return new Blazon(text, font, palette.gradient(), direction, palette.accent(), lines, letterSpacing,
                wordSpacing, margin);
    }

    /**
     * Returns a copy with the given number of blank cells between adjacent
     * glyphs.
     *
     * @param letterSpacing the gap between glyphs, in cells
     * @return the derived banner
     * @throws IllegalArgumentException if {@code letterSpacing} is negative
     */
    public Blazon letterSpacing(int letterSpacing) {
        requireNonNegative(letterSpacing, ERROR_NEGATIVE_SPACING);
        return new Blazon(text, font, gradient, direction, accent, lines, letterSpacing, wordSpacing, margin);
    }

    /**
     * Returns a copy with the given number of extra blank cells contributed by
     * each space in the text, on top of the normal letter spacing.
     *
     * @param wordSpacing the extra gap per space character, in cells
     * @return the derived banner
     * @throws IllegalArgumentException if {@code wordSpacing} is negative
     */
    public Blazon wordSpacing(int wordSpacing) {
        requireNonNegative(wordSpacing, ERROR_NEGATIVE_SPACING);
        return new Blazon(text, font, gradient, direction, accent, lines, letterSpacing, wordSpacing, margin);
    }

    /**
     * Returns a copy with the given number of blank cells prepended to every
     * rendered line.
     *
     * @param margin the left margin, in cells
     * @return the derived banner
     * @throws IllegalArgumentException if {@code margin} is negative
     */
    public Blazon margin(int margin) {
        requireNonNegative(margin, ERROR_NEGATIVE_MARGIN);
        return new Blazon(text, font, gradient, direction, accent, lines, letterSpacing, wordSpacing, margin);
    }

    /**
     * Returns a copy with an additional metadata line appended below the art.
     *
     * @param line the metadata text; may contain placeholders resolved at render
     * time
     * @return the derived banner
     */
    public Blazon line(String line) {
        final List<String> next = new ArrayList<>(lines);
        next.add(line);
        return new Blazon(text, font, gradient, direction, accent, List.copyOf(next), letterSpacing, wordSpacing,
                margin);
    }

    /**
     * Renders the banner, auto-detecting colour support.
     *
     * @return the rendered banner without a trailing newline
     */
    public String render() {
        return render(ColorSupport.autoDetect());
    }

    /**
     * Renders the banner with explicit colour support.
     *
     * @param colorSupport whether to emit ANSI colour escapes
     * @return the rendered banner without a trailing newline
     */
    public String render(ColorSupport colorSupport) {
        return render(colorSupport, UnaryOperator.identity());
    }

    /**
     * Renders the banner, applying a resolver to each metadata line before
     * painting it (used, for example, to expand {@code ${...}} placeholders).
     *
     * @param colorSupport whether to emit ANSI colour escapes
     * @param lineResolver applied to every metadata line prior to colouring
     * @return the rendered banner without a trailing newline
     */
    public String render(ColorSupport colorSupport, UnaryOperator<String> lineResolver) {
        final boolean       colored = colorSupport == ColorSupport.TRUECOLOR;
        final String        indent  = " ".repeat(margin);
        final List<String>  art     = layout();
        final StringBuilder out     = new StringBuilder();
        for (int y = 0; y < art.size(); y++) {
            final String row = art.get(y);
            out.append(indent).append(colored ? paintArtRow(row, y, art.size()) : row).append('\n');
        }
        for (final String raw : lines) {
            final String resolved = lineResolver.apply(raw);
            out.append(indent).append(colored ? accent.foregroundAnsi() + resolved + ANSI_RESET : resolved)
                    .append('\n');
        }
        if (out.length() > 0 && out.charAt(out.length() - 1) == '\n') {
            out.setLength(out.length() - 1);
        }
        return out.toString();
    }

    private List<String> layout() {
        final int                 height = font.height();
        final List<StringBuilder> grid   = new ArrayList<>(height);
        for (int row = 0; row < height; row++) {
            grid.add(new StringBuilder());
        }
        boolean first         = true;
        int     pendingSpaces = 0;
        for (final char character : text.toCharArray()) {
            if (character == ' ') {
                pendingSpaces++;
                continue;
            }
            final Glyph glyph = font.glyph(character);
            if (!first) {
                final int gap = letterSpacing + pendingSpaces * wordSpacing;
                for (final StringBuilder row : grid) {
                    row.append(" ".repeat(gap));
                }
            }
            for (int row = 0; row < height; row++) {
                grid.get(row).append(row < glyph.rows().size() ? glyph.rows().get(row) : " ".repeat(glyph.width()));
            }
            first         = false;
            pendingSpaces = 0;
        }
        return grid.stream().map(StringBuilder::toString).toList();
    }

    private String paintArtRow(String row, int y, int height) {
        final int           width = row.length();
        final StringBuilder out   = new StringBuilder();
        for (int x = 0; x < width; x++) {
            final char cell = row.charAt(x);
            if (cell == ' ') {
                out.append(' ');
            } else {
                out.append(gradient.at(direction.position(x, y, width, height)).foregroundAnsi()).append(cell);
            }
        }
        return out.append(ANSI_RESET).toString();
    }

    private static void requireNonNegative(int value, String message) {
        if (value < 0) {
            throw new IllegalArgumentException(message);
        }
    }

}
