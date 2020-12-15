package com.patriciomascialino.minesweeper.game;

import com.patriciomascialino.minesweeper.utils.Coordinate;
import lombok.Getter;

import java.util.Set;

@Getter
public class ClickResponse {
    private ClickResult clickResult;
    private Set<Coordinate> uncoveredPositions;

    public ClickResponse(ClickResult clickResult, Set<Coordinate> uncoveredPositions) {
        this.clickResult = clickResult;
        this.uncoveredPositions = uncoveredPositions;
    }
}
