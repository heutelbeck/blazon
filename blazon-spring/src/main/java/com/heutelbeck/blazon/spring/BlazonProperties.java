/*
 * Copyright (c) 2026 Dominic Heutelbeck (dominic@heutelbeck.com)
 *
 * SPDX-License-Identifier: MIT
 *
 * Licensed under the MIT License. See the LICENSE file in the project root
 * for the full license text.
 */
package com.heutelbeck.blazon.spring;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Configuration for the automatically installed Blazon startup banner, bound
 * from the {@code blazon.*} properties.
 */
@ConfigurationProperties("blazon")
public class BlazonProperties {

    private boolean enabled  = true;
    private String  text;
    private String  palette  = "EMBER";
    private String  color    = "AUTO";
    private int     margin   = 1;
    private boolean metadata = true;

    /**
     * Creates the properties populated with their defaults.
     */
    public BlazonProperties() {
    }

    /**
     * Whether Blazon replaces the startup banner.
     *
     * @return whether Blazon is enabled; defaults to {@code true}
     */
    public boolean isEnabled() {
        return enabled;
    }

    /**
     * Sets whether Blazon replaces the startup banner.
     *
     * @param enabled the new value
     */
    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    /**
     * The explicit banner text.
     *
     * @return the banner text, or {@code null} to derive it from the application
     * name
     */
    public String getText() {
        return text;
    }

    /**
     * Sets the explicit banner text.
     *
     * @param text the text, or {@code null} to derive it from the application name
     */
    public void setText(String text) {
        this.text = text;
    }

    /**
     * The palette to paint the banner with.
     *
     * @return the {@link com.heutelbeck.blazon.Palettes} name; defaults to
     * {@code EMBER}
     */
    public String getPalette() {
        return palette;
    }

    /**
     * Sets the palette to paint the banner with.
     *
     * @param palette a {@link com.heutelbeck.blazon.Palettes} name
     */
    public void setPalette(String palette) {
        this.palette = palette;
    }

    /**
     * The colour mode.
     *
     * @return {@code AUTO}, {@code TRUECOLOR} or {@code NONE}; defaults to
     * {@code AUTO}
     */
    public String getColor() {
        return color;
    }

    /**
     * Sets the colour mode.
     *
     * @param color {@code AUTO}, {@code TRUECOLOR} or {@code NONE}
     */
    public void setColor(String color) {
        this.color = color;
    }

    /**
     * The left margin prepended to every line.
     *
     * @return the margin in cells; defaults to {@code 1}
     */
    public int getMargin() {
        return margin;
    }

    /**
     * Sets the left margin prepended to every line.
     *
     * @param margin the margin in cells
     */
    public void setMargin(int margin) {
        this.margin = margin;
    }

    /**
     * Whether the version and OS metadata lines are appended.
     *
     * @return whether metadata lines are included; defaults to {@code true}
     */
    public boolean isMetadata() {
        return metadata;
    }

    /**
     * Sets whether the version and OS metadata lines are appended.
     *
     * @param metadata the new value
     */
    public void setMetadata(boolean metadata) {
        this.metadata = metadata;
    }

}
