package com.patriciomascialino.minesweeper.game;

import lombok.AccessLevel;
import lombok.Getter;

import java.util.HashSet;
import java.util.Random;
import java.util.Set;

public class Bombs {
    @Getter(AccessLevel.PROTECTED)
    private Set<Coordinate> bombsPositions;
    private final int bombsCount;

    public Bombs(int boardWidth, int boardHeight, int bombsCount) {
        this.bombsCount = bombsCount;
        this.bombsPositions = new HashSet<>();
        Random random = new Random();
        do {
            int x = random.nextInt(boardWidth);
            int y = random.nextInt(boardHeight);
            this.bombsPositions.add(new Coordinate(x, y));
        } while(this.bombsPositions.size() < bombsCount);
    }

    public Bombs(Set<Coordinate> bombsPositions) {
        this.bombsCount = bombsPositions.size();
        this.bombsPositions = bombsPositions;
    }

    public boolean isBombCell(Coordinate coordinate) {
        return this.bombsPositions.stream()
                .anyMatch(bombPosition -> bombPosition.getX() == coordinate.getX()
                        && bombPosition.getY() == coordinate.getY());
    }

    public int count() {
        return this.bombsCount;
    }
}
