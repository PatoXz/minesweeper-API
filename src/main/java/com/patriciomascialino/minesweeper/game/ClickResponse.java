package com.patriciomascialino.minesweeper.game;

import com.patriciomascialino.minesweeper.utils.Coordinate;
import lombok.Getter;

import java.util.Set;

@Getter
public class ClickResponse {
    private final ClickResult clickResult;
    private final Set<Coordinate> uncoveredPositions;
    private final Set<Coordinate> flaggedPositions;

    public ClickResponse(final ClickResult clickResult,
                         final Set<Coordinate> uncoveredPositions,
                         final Set<Coordinate> flaggedPositions) {
        this.clickResult = clickResult;
        this.uncoveredPositions = uncoveredPositions;
        this.flaggedPositions = flaggedPositions;
    }
}
