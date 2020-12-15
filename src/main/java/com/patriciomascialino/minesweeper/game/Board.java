package com.patriciomascialino.minesweeper.game;

import com.patriciomascialino.minesweeper.exception.BombFoundedException;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.util.*;

@Slf4j
public class Board {
    private final int boardHeight;
    private final int boardWidth;
    private final int cellsWithoutBombs;
    @Getter(AccessLevel.PROTECTED)
    private final Bombs bombs;
    private final Cells cells;

    public Board(final int boardHeight, final int boardWidth, final int bombsCount) {
        this.boardHeight = boardHeight;
        this.boardWidth = boardWidth;
        this.bombs = new Bombs(boardWidth, boardHeight, bombsCount);
        this.cells = new Cells();
        this.cellsWithoutBombs = boardWidth * boardHeight - this.bombs.count();
    }

    public Board(final int boardHeight,
                 final int boardWidth,
                 final Set<Coordinate> bombsPositions,
                 final Set<Coordinate> uncoveredPositions,
                 final Set<Coordinate> flaggedPositions) {
        this.boardHeight = boardHeight;
        this.boardWidth = boardWidth;
        this.bombs = new Bombs(bombsPositions);
        this.cells = new Cells(uncoveredPositions, flaggedPositions);
        this.cellsWithoutBombs = boardWidth * boardHeight - this.bombs.count();
    }

    public ClickResponse click(int x, int y) {
        Coordinate clickedCell = new Coordinate(x, y);
        log.info("Click {}", clickedCell);
        if (cells.isCellUncovered(clickedCell)) {
            return createResponse(ClickResult.ALREADY_UNCOVERED);
        }
        if (cells.isCellFlagged(clickedCell)) {
            return createResponse(ClickResult.FLAGGED);
        }
        if (bombs.isBombCell(clickedCell)) {
            log.info("Bomb hit at {}", clickedCell);
            return createResponse(ClickResult.BOMB);
        }
        uncoverCells(clickedCell);
        if (isGameFinished()) {
            return createResponse(ClickResult.WIN);
        }
        return createResponse(ClickResult.EMPTY_CELL);
    }

    public ClickResponse flag(int x, int y) {
        Coordinate flaggedCell = new Coordinate(x, y);
        log.info("Flag {}", flaggedCell);
        if (cells.isCellUncovered(flaggedCell)) {
            return createResponse(ClickResult.ALREADY_UNCOVERED);
        }
        if (cells.isCellFlagged(flaggedCell)) {
            cells.unflagCell(flaggedCell);
            return createResponse(ClickResult.UNFLAGGED);
        }
        else {
            cells.flagCell(flaggedCell);
            return createResponse(ClickResult.FLAGGED);
        }
    }

    private ClickResponse createResponse(ClickResult clickResult) {
        return new ClickResponse(clickResult, this.cells);
    }

    private void uncoverCells(Coordinate coordinate) {
        if (hasAdjacentBombs(coordinate)) {
            log.info("There's adjacent bombs! Only uncover clicked cell {}", coordinate);
            cells.uncoverCell(coordinate);
        }
        else {
            log.info("No adjacent bombs at {}", coordinate);
            uncoverAdjacentCells(coordinate);
        }
    }
    private boolean hasAdjacentBombs(Coordinate coordinate) {
        return iterateAdjacentCells(coordinate, false);
    }

    private boolean uncoverAdjacentCells(Coordinate coordinate) {
        return iterateAdjacentCells(coordinate, true);
    }

    private boolean iterateAdjacentCells(Coordinate coordinate, boolean uncover) {
        try {
            if (coordinate.getY() > 0)
                iterateAdjacentRow(coordinate.aboveCell(), uncover);

            iterateAdjacentRow(coordinate, uncover);

            if (coordinate.getY() < this.boardWidth - 1)
                iterateAdjacentRow(coordinate.belowCell(), uncover);
        } catch (BombFoundedException e) {
            return true;
        }
        return false;
    }

    private void iterateAdjacentRow(Coordinate coordinate, boolean uncover) throws BombFoundedException {
        if (coordinate.getX() > 0)
            checkAdjacentCell(coordinate.leftCell(), uncover);

        checkAdjacentCell(coordinate, uncover);

        if (coordinate.getX() < this.boardHeight - 1)
            checkAdjacentCell(coordinate.rightCell(), uncover);
    }

    private void checkAdjacentCell(Coordinate coordinate, boolean uncover) throws BombFoundedException {
        if (cells.isCellUncovered(coordinate))
            return;
        if (uncover) {
            cells.uncoverCell(coordinate);
            uncoverCells(coordinate);
        }
        else if (bombs.isBombCell(coordinate)) {
            throw new BombFoundedException();
        }
    }

    private boolean isGameFinished() {
        return this.cells.countUncoveredPositions() == this.cellsWithoutBombs;
    }
}
