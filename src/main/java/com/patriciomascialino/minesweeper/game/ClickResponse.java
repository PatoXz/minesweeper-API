package com.patriciomascialino.minesweeper.game;

import lombok.Getter;

import java.util.Set;

@Getter
public class ClickResponse {
    private final ClickResult clickResult;
    private final Set<Coordinate> uncoveredPositions;
    private final Set<Coordinate> flaggedPositions;

    public ClickResponse(final ClickResult clickResult,
                         final Cells cells) {
        this.clickResult = clickResult;
        this.uncoveredPositions = cells.getUncoveredPositions();
        this.flaggedPositions = cells.getFlaggedPositions();
    }
}
