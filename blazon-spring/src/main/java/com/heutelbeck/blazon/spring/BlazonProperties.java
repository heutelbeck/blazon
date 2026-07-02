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
    private String  palette  = "SAPL";
    private String  color    = "AUTO";
    private int     margin   = 1;
    private boolean metadata = true;

    /**
     * @return whether Blazon replaces the startup banner; defaults to {@code true}
     */
    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    /**
     * @return the explicit banner text, or {@code null} to derive it from the
     * application name
     */
    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    /**
     * @return the {@link com.heutelbeck.blazon.Palettes} name to use; defaults to
     * {@code SAPL}
     */
    public String getPalette() {
        return palette;
    }

    public void setPalette(String palette) {
        this.palette = palette;
    }

    /**
     * @return the colour mode: {@code AUTO}, {@code TRUECOLOR} or {@code NONE};
     * defaults to {@code AUTO}
     */
    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    /**
     * @return the left margin in cells; defaults to {@code 1}
     */
    public int getMargin() {
        return margin;
    }

    public void setMargin(int margin) {
        this.margin = margin;
    }

    /**
     * @return whether to append version and OS metadata lines; defaults to
     * {@code true}
     */
    public boolean isMetadata() {
        return metadata;
    }

    public void setMetadata(boolean metadata) {
        this.metadata = metadata;
    }

}
