package com.patriciomascialino.minesweeper.model;

import lombok.AccessLevel;
import lombok.Getter;
import org.springframework.data.annotation.PersistenceConstructor;
import org.springframework.data.annotation.Transient;

import java.util.HashSet;
import java.util.Random;
import java.util.Set;

public class Bombs {
    @Getter(AccessLevel.PROTECTED)
    private Set<Coordinate> bombsPositions;
    @Transient
    private int bombsCount;

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

    @PersistenceConstructor
    protected Bombs(Set<Coordinate> bombsPositions) {
        this.bombsPositions = bombsPositions;
        this.bombsCount = bombsPositions.size();
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
