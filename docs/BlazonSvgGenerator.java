/*
 * Copyright (c) 2026 Dominic Heutelbeck (dominic@heutelbeck.com)
 *
 * SPDX-License-Identifier: MIT
 *
 * Licensed under the MIT License. See the LICENSE file in the project root
 * for the full license text.
 */

/*
 * Regenerates the coloured banner SVGs embedded in README.md. GitHub sanitises
 * inline HTML/CSS and inline SVG, so the only reliable way to show the truecolor
 * output in the README is a committed image; this tool draws each half-block
 * pixel as a coloured <rect> using the real gradient.
 *
 * Run from the repository root after building the core module:
 *
 *   mvn -q -pl blazon install -DskipTests
 *   java -cp blazon/target/classes docs/BlazonSvgGenerator.java
 *
 * Writes to docs/img/. Edit the calls in main(...) to change what is rendered,
 * then re-run to update the images.
 */

import com.heutelbeck.blazon.Blazon;
import com.heutelbeck.blazon.Color;
import com.heutelbeck.blazon.ColorSupport;
import com.heutelbeck.blazon.Fonts;
import com.heutelbeck.blazon.Gradient;
import com.heutelbeck.blazon.Palette;
import com.heutelbeck.blazon.Palettes;

import java.nio.file.Files;
import java.nio.file.Path;

public final class BlazonSvgGenerator {

    private static final String BG      = "#0d1117";
    private static final String BORDER  = "#30363d";
    private static final int    PAD_X   = 24;
    private static final int    PAD_TOP = 24;
    private static final int    PAD_BOT = 24;
    private static final int    GAP     = 18;

    private static final String OUTPUT_DIR = "docs/img";

    private BlazonSvgGenerator() {
    }

    public static void main(String[] args) throws Exception {
        final Path dir = Path.of(args.length > 0 ? args[0] : OUTPUT_DIR);
        Files.createDirectories(dir);

        banner(dir.resolve("title.svg"), 24, "BLAZON", Palettes.SUNSET);
        banner(dir.resolve("quickstart.svg"), 15, "BLAZON", Palettes.EMBER, "Version: 1.0.0",
                "OS:      Linux 6.18 amd64");
        banner(dir.resolve("spring.svg"), 15, "MY APP", Palettes.EMBER, "Version: 1.2.3 (a1b2c3d)",
                "OS:      Linux 6.18.0 amd64");

        final Palette[] palettes = { Palettes.EMBER, Palettes.OCEAN, Palettes.FOREST, Palettes.SUNSET,
                Palettes.MONOCHROME };
        final String[]  names    = { "EMBER", "OCEAN", "FOREST", "SUNSET", "MONOCHROME" };
        palettes(dir.resolve("palettes.svg"), 13, "BLAZON", palettes, names);

        // Neutral "diagram" banners illustrating spacing and glyph shapes.
        final Palette mono = new Palette(Gradient.linear(new Color(196, 203, 212), new Color(130, 140, 152)),
                new Color(150, 160, 170));
        banner(dir.resolve("words.svg"), 13, Blazon.of("TWO WORDS"), mono);
        banner(dir.resolve("words-tight.svg"), 13, Blazon.of("TWO WORDS").wordSpacing(0), mono);
        banner(dir.resolve("letter-spacing.svg"), 13, Blazon.of("WIDE").letterSpacing(3), mono);
        banner(dir.resolve("digits.svg"), 13, Blazon.of("0123456789"), mono);
    }

    private static void banner(Path file, int px, String text, Palette palette, String... meta) throws Exception {
        Blazon blazon = Blazon.of(text).margin(0);
        for (final String line : meta) {
            blazon = blazon.line(line);
        }
        banner(file, px, blazon, palette);
    }

    private static void banner(Path file, int px, Blazon banner, Palette palette) throws Exception {
        final String[] lines  = banner.margin(0).render(ColorSupport.NONE).split("\n", -1);
        final int      height = Fonts.halfBlock().height();
        final int      width  = artWidth(lines, height);
        final int      artW   = width * px;
        final int      artH   = height * 2 * px;
        final int      metaFs = Math.max(13, (int) Math.round(px * 1.5));
        final int      metaLh = (int) Math.round(metaFs * 1.55);

        final int metaCount = lines.length - height;
        int       metaChars = 0;
        for (int i = 0; i < metaCount; i++) {
            metaChars = Math.max(metaChars, lines[height + i].length());
        }
        final int contentW  = Math.max(artW, (int) (metaChars * metaFs * 0.62));
        final int w         = PAD_X * 2 + contentW;
        final int artBottom = PAD_TOP + artH;
        final int h         = artBottom + (metaCount > 0 ? GAP + metaCount * metaLh : 0) + PAD_BOT;

        final StringBuilder svg = new StringBuilder();
        open(svg, w, h);
        chrome(svg, w, h);
        artRects(svg, lines, height, palette.gradient(), px, PAD_X, PAD_TOP);
        final String accent = hex(palette.accent());
        for (int i = 0; i < metaCount; i++) {
            final int y = artBottom + GAP + metaFs + i * metaLh;
            svg.append(String.format(
                    "<text x='%d' y='%d' fill='%s' font-size='%d' xml:space='preserve'>%s</text>%n", PAD_X, y, accent,
                    metaFs, escape(lines[height + i])));
        }
        svg.append("</svg>\n");
        write(file, svg.toString(), w, h);
    }

    private static void palettes(Path file, int px, String text, Palette[] palettes, String[] names) throws Exception {
        final int      height  = Fonts.halfBlock().height();
        final String[] lines   = Blazon.of(text).margin(0).render(ColorSupport.NONE).split("\n", -1);
        final int      width   = artWidth(lines, height);
        final int      artW    = width * px;
        final int      artH    = height * 2 * px;
        final int      labelFs = (int) Math.round(px * 1.5);

        int labelW = 0;
        for (final String name : names) {
            labelW = Math.max(labelW, (int) (name.length() * labelFs * 0.62));
        }
        labelW += 24;

        final int rowGap = 16;
        final int rowH   = artH + rowGap;
        final int w      = PAD_X * 2 + labelW + artW;
        final int h      = PAD_TOP + palettes.length * rowH - rowGap + PAD_BOT;

        final StringBuilder svg = new StringBuilder();
        open(svg, w, h);
        chrome(svg, w, h);
        for (int i = 0; i < palettes.length; i++) {
            final int oy = PAD_TOP + i * rowH;
            svg.append(String.format("<text x='%d' y='%d' fill='%s' font-size='%d'>%s</text>%n", PAD_X,
                    oy + artH / 2 + labelFs / 3, hex(palettes[i].accent()), labelFs, escape(names[i])));
            artRects(svg, lines, height, palettes[i].gradient(), px, PAD_X + labelW, oy);
        }
        svg.append("</svg>\n");
        write(file, svg.toString(), w, h);
    }

    private static void artRects(StringBuilder svg, String[] lines, int height, Gradient gradient, int px, int ox,
            int oy) {
        final int width = artWidth(lines, height);
        for (int ty = 0; ty < height; ty++) {
            final String line = lines[ty];
            for (int x = 0; x < line.length(); x++) {
                final char cell = line.charAt(x);
                if (cell == ' ') {
                    continue;
                }
                final String fill = hex(gradient.at(width <= 1 ? 0.0 : (double) x / (width - 1)));
                if (cell == '█' || cell == '▀') {
                    rect(svg, ox + x * px, oy + (ty * 2) * px, px, fill);
                }
                if (cell == '█' || cell == '▄') {
                    rect(svg, ox + x * px, oy + (ty * 2 + 1) * px, px, fill);
                }
            }
        }
    }

    private static void open(StringBuilder svg, int w, int h) {
        svg.append(String.format(
                "<svg xmlns='http://www.w3.org/2000/svg' width='%d' height='%d' viewBox='0 0 %d %d' "
                        + "font-family='ui-monospace,SFMono-Regular,Menlo,Consolas,monospace'>%n",
                w, h, w, h));
    }

    private static void chrome(StringBuilder svg, int w, int h) {
        svg.append(String.format("<rect x='0.5' y='0.5' width='%d' height='%d' rx='12' fill='%s' stroke='%s'/>", w - 1,
                h - 1, BG, BORDER));
    }

    private static void rect(StringBuilder svg, int x, int y, int px, String fill) {
        svg.append(String.format("<rect x='%d' y='%d' width='%d' height='%d' fill='%s'/>", x, y, px, px, fill));
    }

    private static int artWidth(String[] lines, int height) {
        int width = 0;
        for (int i = 0; i < height; i++) {
            width = Math.max(width, lines[i].length());
        }
        return width;
    }

    private static String hex(Color color) {
        return String.format("#%02x%02x%02x", color.red(), color.green(), color.blue());
    }

    private static String escape(String text) {
        return text.replace("&", "&amp;").replace("<", "&lt;").replace(">", "&gt;");
    }

    private static void write(Path file, String content, int w, int h) throws Exception {
        Files.writeString(file, content);
        System.out.printf("wrote %s (%dx%d)%n", file, w, h);
    }

}
