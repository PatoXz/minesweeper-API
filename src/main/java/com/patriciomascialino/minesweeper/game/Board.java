package com.patriciomascialino.minesweeper.game;

import com.patriciomascialino.minesweeper.utils.Coordinate;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.util.*;

@Slf4j
public class Board {
    private final int boardHeight;
    private final int boardWidth;
    @Getter(AccessLevel.PROTECTED)
    private Set<Coordinate> bombsPositions;
    private Set<Coordinate> uncoveredPositions;

    private Board(final int boardHeight, final int boardWidth) {
        this.boardHeight = boardHeight;
        this.boardWidth = boardWidth;
    }

    public Board(final int boardHeight, final int boardWidth, final int bombsCount) {
        this(boardHeight, boardWidth);
        setBombs(bombsCount);
        this.uncoveredPositions = new HashSet<>();
    }

    public Board(final int boardHeight, final int boardWidth, final Set<Coordinate> bombsPositions, Set<Coordinate> uncoveredPositions) {
        this(boardHeight, boardWidth);
        this.bombsPositions = bombsPositions;
        this.uncoveredPositions = uncoveredPositions;
    }

    protected void setBombs(int bombsCount) {
        Set<Coordinate> bombsPositions = new HashSet<>();
        Random random = new Random();
        do {
            int x = random.nextInt(this.boardWidth + 1);
            int y = random.nextInt(this.boardHeight + 1);
            bombsPositions.add(new Coordinate(x, y));
        } while(bombsPositions.size() < bombsCount);
        this.bombsPositions = bombsPositions;
    }

    public ClickResponse click(int x, int y) {
        log.info("Click {}, {}", x, y);
        if (isCellUncovered(x, y)) {
            return new ClickResponse(ClickResult.ALREADY_UNCOVERED, uncoveredPositions);
        }
        final boolean hitBomb = isBombCell(x, y);
        if (hitBomb) {
            log.info("Bomb hit at ({}, {})", x, y);
            return new ClickResponse(ClickResult.BOMB, uncoveredPositions);
        }
        uncoverCell(x, y);
        return new ClickResponse(ClickResult.EMPTY_CELL, uncoveredPositions);
    }

    private void uncoverCell(int x, int y) {
        if (hasAdjacentBombs(x, y)) {
            log.info("There's adjacent bombs! Only uncover clicked cell");
            uncoveredPositions.add(new Coordinate(x, y));
        }
        else {
            log.info("No adjacent bombs!");
            uncoverAdjacentCells(x, y);
        }
    }
    private boolean hasAdjacentBombs(int x, int y) {
        return iterateAdjacentCells(x, y, false);
    }

    private boolean uncoverAdjacentCells(int x, int y) {
        return iterateAdjacentCells(x, y, true);
    }

    private boolean iterateAdjacentCells(int x, int y, boolean uncover) {
        try {
            if (y > 0)
                iterateAdjacentRow(x, y - 1, uncover);

            iterateAdjacentRow(x, y, uncover);

            if (y < this.boardWidth - 1)
                iterateAdjacentRow(x, y + 1, uncover);
        } catch (RuntimeException e) {
            return true;
        }
        return false;
    }

    private void iterateAdjacentRow(int x, int y, boolean uncover) {
        if (x > 0)
            checkAdjacentCell(x - 1, y, uncover);

        checkAdjacentCell(x, y, uncover);

        if (x < this.boardHeight - 1)
            checkAdjacentCell(x + 1, y, uncover);
    }

    private void checkAdjacentCell(int x, int y, boolean uncover) {
        if (isCellUncovered(x, y))
            return;
        if (uncover) {
            uncoveredPositions.add(new Coordinate(x, y));
            uncoverCell(x, y);
        }
        else if (isBombCell(x, y)) {
            throw new RuntimeException();
        }
    }

    private boolean isBombCell(int x, int y) {
        return this.bombsPositions.stream()
                .anyMatch(bombPosition -> bombPosition.getX() == x && bombPosition.getY() == y);
    }

    private boolean isCellUncovered(int x, int y) {
        return this.uncoveredPositions.stream()
                .anyMatch(uncoveredPosition -> uncoveredPosition.getX() == x && uncoveredPosition.getY() == y);
    }
}