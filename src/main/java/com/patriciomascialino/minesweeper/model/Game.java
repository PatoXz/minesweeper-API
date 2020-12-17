package com.patriciomascialino.minesweeper.model;

import com.patriciomascialino.minesweeper.exception.BombFoundedException;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.PersistenceConstructor;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;

@Document(collection = "games")
@Getter
@Slf4j
public class Game {
    @Id
    private ObjectId gameId;
    private final int boardHeight;
    private final int boardWidth;
    @Transient
    @Getter(AccessLevel.PROTECTED)
    private final int cellsWithoutBombs;
    @Getter(AccessLevel.PROTECTED)
    private final Bombs bombs;
    private final Cells cells;
    private GameStatus gameStatus;
    private final Instant gameStartedAt;
    private Instant gameFinishedAt;

    public Game(final int boardHeight, final int boardWidth, final int bombsCount) {
        this.boardHeight = boardHeight;
        this.boardWidth = boardWidth;
        this.bombs = new Bombs(boardWidth, boardHeight, bombsCount);
        this.cells = new Cells();
        this.cellsWithoutBombs = boardWidth * boardHeight - this.bombs.count();
        this.gameStatus = GameStatus.PLAYING;
        this.gameStartedAt = ZonedDateTime.now(ZoneOffset.UTC).toInstant();
    }

    @PersistenceConstructor
    public Game(final ObjectId gameId,
                final int boardHeight,
                final int boardWidth,
                Bombs bombs,
                Cells cells,
                GameStatus gameStatus,
                Instant gameStartedAt) {
        this.gameId = gameId;
        this.boardHeight = boardHeight;
        this.boardWidth = boardWidth;
        this.bombs = bombs;
        this.cells = cells;
        this.cellsWithoutBombs = boardWidth * boardHeight - this.bombs.count();
        this.gameStatus = gameStatus;
        this.gameStartedAt = gameStartedAt;
    }

    public ClickResult click(Coordinate coordinate) {
        if (this.gameStatus.isFinished())
            return ClickResult.GAME_ALREADY_FINISHED;
        log.info("Click {}", coordinate);
        if (cells.isCellUncovered(coordinate)) {
            return ClickResult.ALREADY_UNCOVERED;
        }
        if (cells.isCellFlagged(coordinate)) {
            return ClickResult.FLAGGED;
        }
        if (bombs.isBombCell(coordinate)) {
            log.info("Bomb hit at {}", coordinate);
            this.gameFinishedAt = ZonedDateTime.now(ZoneOffset.UTC).toInstant();
            this.gameStatus = GameStatus.LOST;
            return ClickResult.BOMB;
        }
        uncoverCells(coordinate);
        if (isGameFinished()) {
            this.gameFinishedAt = ZonedDateTime.now(ZoneOffset.UTC).toInstant();
            this.gameStatus = GameStatus.WON;
            return ClickResult.WIN;
        }
        return ClickResult.EMPTY_CELL;
    }

    public ClickResult flag(Coordinate coordinate) {
        if (this.gameStatus.isFinished())
            return ClickResult.GAME_ALREADY_FINISHED;
        log.info("Flag {}", coordinate);
        if (cells.isCellUncovered(coordinate)) {
            return ClickResult.ALREADY_UNCOVERED;
        }
        if (cells.isCellFlagged(coordinate)) {
            cells.unflagCell(coordinate);
            return ClickResult.UNFLAGGED;
        }
        else {
            cells.flagCell(coordinate);
            return ClickResult.FLAGGED;
        }
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

    public int getBombsCount() {
        return this.bombs.count();
    }

    public ZonedDateTime getGameStartedAt() {
        return ZonedDateTime.ofInstant(gameStartedAt, ZoneOffset.UTC);
    }

    public ZonedDateTime getGameFinishedAt() {
        if (gameFinishedAt != null) {
            return ZonedDateTime.ofInstant(gameFinishedAt, ZoneOffset.UTC);
        }
        return null;
    }
}
