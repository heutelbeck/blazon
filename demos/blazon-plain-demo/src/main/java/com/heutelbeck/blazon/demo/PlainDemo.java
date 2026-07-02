/*
 * Copyright (c) 2026 Dominic Heutelbeck (dominic@heutelbeck.com)
 *
 * SPDX-License-Identifier: MIT
 *
 * Licensed under the MIT License. See the LICENSE file in the project root
 * for the full license text.
 */
package com.heutelbeck.blazon.demo;

import com.heutelbeck.blazon.Blazon;
import com.heutelbeck.blazon.Color;
import com.heutelbeck.blazon.ColorSupport;
import com.heutelbeck.blazon.Direction;
import com.heutelbeck.blazon.Gradient;
import com.heutelbeck.blazon.Palette;
import com.heutelbeck.blazon.Palettes;

/**
 * Prints "BLAZON BANNERS" in a range of styles, showing the settings and colour
 * choices next to each example. Rendered in truecolor so the gradients are
 * visible regardless of how output is captured.
 */
public final class PlainDemo {

    private static final String ANSI_RESET = "\u001b[0m";
    private static final String TEXT       = "BLAZON BANNERS";

    private PlainDemo() {
    }

    /**
     * Renders every example to standard output.
     *
     * @param args ignored
     */
    public static void main(String[] args) {
        palette("EMBER", Palettes.EMBER, Direction.HORIZONTAL, 1);
        palette("OCEAN (diagonal)", Palettes.OCEAN, Direction.DIAGONAL, 1);
        palette("FOREST (vertical)", Palettes.FOREST, Direction.VERTICAL, 1);
        palette("SUNSET (three-stop)", Palettes.SUNSET, Direction.HORIZONTAL, 1);
        palette("MONOCHROME (wide spacing)", Palettes.MONOCHROME, Direction.HORIZONTAL, 3);
        custom("Custom red to yellow (diagonal)", Gradient.linear(new Color(220, 40, 40), new Color(250, 210, 60)),
                new Color(250, 230, 160), Direction.DIAGONAL, 1);
        custom("Solid cyan (tight spacing)", Gradient.solid(new Color(80, 200, 220)), new Color(80, 200, 220),
                Direction.HORIZONTAL, 0);
        glyphs();
    }

    private static void glyphs() {
        final String   characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        final int      perRow     = 6;
        final Color    light      = new Color(186, 230, 253);
        final Gradient gradient   = Gradient.linear(light, new Color(129, 212, 214));

        System.out.println(light.foregroundAnsi() + "== Default font glyphs (6 per row) ==" + ANSI_RESET);
        for (int start = 0; start < characters.length(); start += perRow) {
            final String row = characters.substring(start, Math.min(start + perRow, characters.length()));
            System.out.println(
                    Blazon.of(row).gradient(gradient).letterSpacing(2).margin(1).render(ColorSupport.TRUECOLOR));
            System.out.println();
        }
    }

    private static void palette(String title, Palette palette, Direction direction, int letterSpacing) {
        custom(title, palette.gradient(), palette.accent(), direction, letterSpacing);
    }

    private static void custom(String title, Gradient gradient, Color accent, Direction direction, int letterSpacing) {
        final Blazon banner = Blazon.of(TEXT).gradient(gradient).accent(accent).direction(direction)
                .letterSpacing(letterSpacing).margin(1).line(title);

        System.out.println(accent.foregroundAnsi() + "== " + title + " ==" + ANSI_RESET);
        System.out.println("   gradient: start " + rgb(gradient.at(0.0)) + "  mid " + rgb(gradient.at(0.5)) + "  end "
                + rgb(gradient.at(1.0)));
        System.out.println(
                "   accent: " + rgb(accent) + "   direction: " + direction + "   letterSpacing: " + letterSpacing);
        System.out.println(banner.render(ColorSupport.TRUECOLOR));
        System.out.println();
    }

    private static String rgb(Color color) {
        return "(" + color.red() + "," + color.green() + "," + color.blue() + ")";
    }

}
