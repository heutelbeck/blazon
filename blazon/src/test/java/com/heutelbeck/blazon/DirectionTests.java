/*
 * Copyright (c) 2026 Dominic Heutelbeck (dominic@heutelbeck.com)
 *
 * SPDX-License-Identifier: MIT
 *
 * Licensed under the MIT License. See the LICENSE file in the project root
 * for the full license text.
 */
package com.heutelbeck.blazon;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.within;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("Direction")
class DirectionTests {

    private static final double TOLERANCE = 1e-9;

    @Test
    @DisplayName("HORIZONTAL varies with the column across the width")
    void whenHorizontalThenColumnFraction() {
        assertThat(Direction.HORIZONTAL.position(0, 3, 5, 2)).isCloseTo(0.0, within(TOLERANCE));
        assertThat(Direction.HORIZONTAL.position(4, 3, 5, 2)).isCloseTo(1.0, within(TOLERANCE));
        assertThat(Direction.HORIZONTAL.position(2, 3, 5, 2)).isCloseTo(0.5, within(TOLERANCE));
    }

    @Test
    @DisplayName("VERTICAL varies with the row across the height")
    void whenVerticalThenRowFraction() {
        assertThat(Direction.VERTICAL.position(9, 0, 5, 3)).isCloseTo(0.0, within(TOLERANCE));
        assertThat(Direction.VERTICAL.position(9, 2, 5, 3)).isCloseTo(1.0, within(TOLERANCE));
    }

    @Test
    @DisplayName("DIAGONAL varies with column plus row")
    void whenDiagonalThenCombinedFraction() {
        assertThat(Direction.DIAGONAL.position(0, 0, 3, 3)).isCloseTo(0.0, within(TOLERANCE));
        assertThat(Direction.DIAGONAL.position(2, 2, 3, 3)).isCloseTo(1.0, within(TOLERANCE));
        assertThat(Direction.DIAGONAL.position(2, 0, 3, 3)).isCloseTo(0.5, within(TOLERANCE));
    }

    @Test
    @DisplayName("degenerate single-cell spans map to zero")
    void whenSpanIsSingleCellThenZero() {
        assertThat(Direction.HORIZONTAL.position(0, 0, 1, 1)).isZero();
        assertThat(Direction.VERTICAL.position(0, 0, 1, 1)).isZero();
        assertThat(Direction.DIAGONAL.position(0, 0, 1, 1)).isZero();
    }

}
