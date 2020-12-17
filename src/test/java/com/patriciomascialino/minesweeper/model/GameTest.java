package com.patriciomascialino.minesweeper.model;

import org.bson.types.ObjectId;
import org.junit.jupiter.api.Test;

import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

public class GameTest {
    @Test
    public void bombHitTest() {
        Game game = new Game(10, 10, 1);
        final Coordinate first = game.getBombs().getBombsPositions().stream().findFirst().orElseThrow(RuntimeException::new);

        ClickResult clickResult = game.click(first);
        assertEquals(ClickResult.BOMB, clickResult);
    }

    @Test
    public void clickCellTwiceTest() {
        Set<Coordinate> bombsPositions = new HashSet<>();
        bombsPositions.add(new Coordinate(0, 0));
        Game game = givingAGame(2, bombsPositions);

        final Coordinate coordinate = new Coordinate(1, 1);
        ClickResult clickResult = game.click(coordinate);
        assertEquals(ClickResult.EMPTY_CELL, clickResult);

        clickResult = game.click(coordinate);
        assertEquals(ClickResult.ALREADY_UNCOVERED, clickResult);

        assertEquals(1, game.getCells().countUncoveredPositions());
        assertTrue(game.getCells().getUncoveredPositions().stream()
                .anyMatch(uncoveredPosition -> uncoveredPosition.getX() == 1 && uncoveredPosition.getY() == 1));
    }

    @Test
    public void cellWithAdjacentBombTest() {
        Game game = givingAGameWithABomb(10);

        final Coordinate coordinate = new Coordinate(2, 2);
        ClickResult clickResult = game.click(coordinate);
        assertEquals(ClickResult.EMPTY_CELL, clickResult);

        assertEquals(1, game.getCells().countUncoveredPositions());
        assertTrue(game.getCells().isCellUncovered(coordinate));
    }

    @Test
    public void cellWithNoAdjacentBombAndOneBombTest() {
        Game game = givingAGameWithABomb(5);

        final Coordinate coordinate = new Coordinate(3, 3);
        ClickResult clickResult = game.click(coordinate);
        assertEquals(ClickResult.EMPTY_CELL, clickResult);

        assertTrue(game.getCells().isCellUncovered(coordinate));
        assertTrue(game.getCells().isCellUncovered(coordinate.aboveCell().leftCell()));
        assertEquals(21, game.getCells().countUncoveredPositions());
    }

    @Test
    public void cellWithNoAdjacentBombAndTwoBombsTest() {
        Set<Coordinate> bombsPositions = new HashSet<>();
        bombsPositions.add(new Coordinate(1, 1));
        bombsPositions.add(new Coordinate(4, 4));
        Game game = givingAGame(5, bombsPositions);

        final Coordinate coordinate = new Coordinate(0, 4);
        ClickResult clickResult = game.click(coordinate);
        assertEquals(ClickResult.EMPTY_CELL, clickResult);

        assertEquals(20, game.getCells().countUncoveredPositions());
    }

    @Test
    public void cellWithNoAdjacentBombAndTwoBombs2Test() {
        Set<Coordinate> bombsPositions = new HashSet<>();
        bombsPositions.add(new Coordinate(1, 1));
        bombsPositions.add(new Coordinate(3, 3));
        Game game = givingAGame(5, bombsPositions);

        final Coordinate coordinate = new Coordinate(0, 4);
        ClickResult clickResult = game.click(coordinate);
        assertEquals(ClickResult.EMPTY_CELL, clickResult);

        assertEquals(9, game.getCells().countUncoveredPositions());
    }

    @Test
    public void cellWithNoAdjacentBombAndTwoBombs3Test() {
        Set<Coordinate> bombsPositions = new HashSet<>();
        bombsPositions.add(new Coordinate(1, 1));
        bombsPositions.add(new Coordinate(3, 1));
        Game game = givingAGame(5, bombsPositions);

        final Coordinate coordinate = new Coordinate(0, 4);
        ClickResult clickResult = game.click(coordinate);
        assertEquals(ClickResult.EMPTY_CELL, clickResult);

        assertEquals(15, game.getCells().countUncoveredPositions());
    }

    @Test
    public void cellWithNoAdjacentBombAndThreeBombsTest() {
        Set<Coordinate> bombsPositions = new HashSet<>();
        bombsPositions.add(new Coordinate(1, 1));
        bombsPositions.add(new Coordinate(3, 1));
        bombsPositions.add(new Coordinate(2, 3));
        Game game = givingAGame(5, bombsPositions);

        final Coordinate coordinate = new Coordinate(0, 4);
        ClickResult clickResult = game.click(coordinate);
        assertEquals(ClickResult.EMPTY_CELL, clickResult);

        assertEquals(6, game.getCells().countUncoveredPositions());
    }

    @Test
    public void clickFlaggedCellTest() {
        Game game = givingAGameWithABomb(2);

        final Coordinate coordinate = new Coordinate(0, 0);

        ClickResult clickResult = game.flag(coordinate);
        assertEquals(ClickResult.FLAGGED, clickResult);
        assertEquals(1, game.getCells().getFlaggedPositions().size());
        assertEquals(0, game.getCells().countUncoveredPositions());
        assertTrue(game.getCells().isCellFlagged(coordinate));
    }

    @Test
    public void flagCellTest() {
        Game game = givingAGameWithABomb(2);

        final Coordinate coordinate = new Coordinate(0, 0);
        ClickResult clickResult = game.flag(coordinate);
        assertEquals(ClickResult.FLAGGED, clickResult);
        assertEquals(1, game.getCells().getFlaggedPositions().size());
    }

    @Test
    public void removeFlagCellTest() {
        Game game = givingAGameWithABomb(2);

        final Coordinate coordinate = new Coordinate(0, 0);
        ClickResult clickResult = game.flag(coordinate);
        assertEquals(ClickResult.FLAGGED, clickResult);
        assertEquals(1, game.getCells().getFlaggedPositions().size());
        assertTrue(game.getCells().isCellFlagged(coordinate));

        clickResult = game.flag(coordinate);
        assertEquals(ClickResult.UNFLAGGED, clickResult);
        assertEquals(0, game.getCells().getFlaggedPositions().size());
    }

    @Test
    public void flagUncoveredCellTest() {
        Game game = givingAGameWithABomb(2);

        final Coordinate coordinate = new Coordinate(0, 0);
        ClickResult clickResult = game.click(coordinate);
        assertEquals(ClickResult.EMPTY_CELL, clickResult);
        assertEquals(0, game.getCells().getFlaggedPositions().size());

        clickResult = game.flag(coordinate);
        assertEquals(ClickResult.ALREADY_UNCOVERED, clickResult);
        assertEquals(0, game.getCells().getFlaggedPositions().size());
    }

    @Test
    public void winGameTest() {
        Game game = givingAGameWithABomb(2);

        ClickResult clickResult = game.click(new Coordinate(0, 0));
        assertEquals(ClickResult.EMPTY_CELL, clickResult);

        clickResult = game.click(new Coordinate(0, 1));
        assertEquals(ClickResult.EMPTY_CELL, clickResult);

        clickResult = game.click(new Coordinate(1, 0));
        assertEquals(ClickResult.WIN, clickResult);
        assertEquals(GameStatus.WON, game.getGameStatus());
    }

    @Test
    public void lostGameTest() {
        Game game = givingAGameWithABomb(2);

        ClickResult clickResult = game.click(new Coordinate(1, 1));
        assertEquals(ClickResult.BOMB, clickResult);
        assertEquals(GameStatus.LOST, game.getGameStatus());
    }

    @Test
    public void bombCountTest() {
        Game game = givingAGameWithABomb(2);
        assertEquals(1, game.getBombsCount());
        assertEquals(1, game.getBombs().getBombsPositions().size());
    }

    @Test
    public void gameAlreadyFinishedOnClickTest() {
        Game game = givingAGameWithABomb(2);
        ClickResult click = game.click(new Coordinate(0, 0));
        assertEquals(ClickResult.EMPTY_CELL, click);

        click = game.click(new Coordinate(0, 1));
        assertEquals(ClickResult.EMPTY_CELL, click);

        click = game.click(new Coordinate(1, 0));
        assertEquals(ClickResult.WIN, click);
        assertEquals(GameStatus.WON, game.getGameStatus());
        assertTrue(game.getGameStatus().isFinished());

        click = game.click(new Coordinate(1, 0));
        assertEquals(ClickResult.GAME_ALREADY_FINISHED, click);
    }

    @Test
    public void gameAlreadyFinishedOnFlagTest() {
        Set<Coordinate> bombsPositions = new HashSet<>();
        bombsPositions.add(new Coordinate(1, 1));

        Set<Coordinate> uncoveredPositions = new HashSet<>();
        uncoveredPositions.add(new Coordinate(0, 0));
        uncoveredPositions.add(new Coordinate(0, 1));

        Game game = new Game(new ObjectId(), 2, 2,
                new Bombs(bombsPositions), new Cells(uncoveredPositions,
                new HashSet<>()), GameStatus.PLAYING, ZonedDateTime.now(ZoneOffset.UTC).toInstant());

        ClickResult click = game.click(new Coordinate(1, 0));
        assertEquals(ClickResult.WIN, click);
        assertTrue(game.getGameStatus().isFinished());

        click = game.flag(new Coordinate(1, 0));
        assertEquals(ClickResult.GAME_ALREADY_FINISHED, click);
    }

    @Test
    public void clickOnFlaggedCellReturnsFlaggedCellTest() {
        Game game = givingAGameWithABomb(2);
        ClickResult click = game.flag(new Coordinate(0, 0));
        assertEquals(ClickResult.FLAGGED, click);

        click = game.click(new Coordinate(0, 0));
        assertEquals(ClickResult.FLAGGED, click);
    }

    @Test
    public void gamePropertiesTest() {
        Set<Coordinate> bombsPositions = new HashSet<>();
        bombsPositions.add(new Coordinate(1, 1));
        final ObjectId gameId = new ObjectId();
        Game game = new Game(gameId, 3, 2, new Bombs(bombsPositions),
                new Cells(), GameStatus.PLAYING, ZonedDateTime.now(ZoneOffset.UTC).toInstant());
        assertEquals(3, game.getBoardHeight());
        assertEquals(2, game.getBoardWidth());
        assertEquals(gameId.toString(), game.getGameId().toString());
        assertEquals(5, game.getCellsWithoutBombs());
    }

    @Test
    public void gameStartedAtWithExistingGameTest() {
        Game game = givingAGameWithABomb(2);
        assertNotNull(game.getGameStartedAt());
        assertEquals(ZonedDateTime.parse("2020-12-16T00:00:00Z"), game.getGameStartedAt());
    }

    @Test
    public void gameStartedAtWithNewGameTest() {
        Game game = new Game(2, 2, 1);
        assertNotNull(game.getGameStartedAt());
    }

    private Game givingAGameWithABomb(int size) {
        Set<Coordinate> bombsPositions = new HashSet<>();
        bombsPositions.add(new Coordinate(1, 1));
        return givingAGame(size, bombsPositions);
    }

    private Game givingAGame(int size, Set<Coordinate> bombsPositions) {
        return new Game(new ObjectId(), size, size, new Bombs(bombsPositions),
                new Cells(), GameStatus.PLAYING, ZonedDateTime.parse("2020-12-16T00:00:00Z").toInstant());
    }
}
