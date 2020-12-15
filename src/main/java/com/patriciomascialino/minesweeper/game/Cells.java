package com.patriciomascialino.minesweeper.game;

import lombok.Getter;

import java.util.HashSet;
import java.util.Set;

@Getter
public class Cells {
    private Set<Coordinate> uncoveredPositions;
    private Set<Coordinate> flaggedPositions;

    public Cells() {
        this.uncoveredPositions = new HashSet<>();
        this.flaggedPositions = new HashSet<>();
    }

    public Cells(Set<Coordinate> uncoveredPositions, Set<Coordinate> flaggedPositions) {
        this.uncoveredPositions = uncoveredPositions;
        this.flaggedPositions = flaggedPositions;
    }

    public void uncoverCell(Coordinate coordinate) {
        this.uncoveredPositions.add(coordinate);
    }

    public void flagCell(Coordinate coordinate) {
        this.flaggedPositions.add(coordinate);
    }

    public void unflagCell(Coordinate coordinate) {
        this.flaggedPositions.removeIf(flaggedPosition -> flaggedPosition.equals(coordinate));
    }

    public boolean isCellUncovered(Coordinate coordinate) {
        return this.uncoveredPositions.stream()
                .anyMatch(uncoveredPosition -> uncoveredPosition.equals(coordinate));
    }

    public boolean isCellFlagged(Coordinate coordinate) {
        return this.flaggedPositions.stream()
                .anyMatch(uncoveredPosition -> uncoveredPosition.equals(coordinate));
    }

    public int countUncoveredPositions() {
        return this.uncoveredPositions.size();
    }
}
