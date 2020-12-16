package com.patriciomascialino.minesweeper.model;

import org.bson.types.ObjectId;
import org.junit.jupiter.api.Test;

import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

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
        Game game = givingABoard(2, bombsPositions);

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
        Set<Coordinate> bombsPositions = new HashSet<>();
        bombsPositions.add(new Coordinate(1, 1));
        Game game = givingABoard(10, bombsPositions);

        final Coordinate coordinate = new Coordinate(2, 2);
        ClickResult clickResult = game.click(coordinate);
        assertEquals(ClickResult.EMPTY_CELL, clickResult);

        assertEquals(1, game.getCells().countUncoveredPositions());
        assertTrue(game.getCells().isCellUncovered(coordinate));
    }

    @Test
    public void cellWithNoAdjacentBombAndOneBombTest() {
        Set<Coordinate> bombsPositions = new HashSet<>();
        bombsPositions.add(new Coordinate(1, 1));
        Game game = givingABoard(5, bombsPositions);

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
        Game game = givingABoard(5, bombsPositions);

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
        Game game = givingABoard(5, bombsPositions);

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
        Game game = givingABoard(5, bombsPositions);

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
        Game game = givingABoard(5, bombsPositions);

        final Coordinate coordinate = new Coordinate(0, 4);
        ClickResult clickResult = game.click(coordinate);
        assertEquals(ClickResult.EMPTY_CELL, clickResult);

        assertEquals(6, game.getCells().countUncoveredPositions());
    }

    @Test
    public void clickFlaggedCellTest() {
        Set<Coordinate> bombsPositions = new HashSet<>();
        bombsPositions.add(new Coordinate(1, 1));
        Game game = givingABoard(2, bombsPositions);

        final Coordinate coordinate = new Coordinate(0, 0);

        ClickResult clickResult = game.flag(coordinate);
        assertEquals(ClickResult.FLAGGED, clickResult);
        assertEquals(1, game.getCells().getFlaggedPositions().size());
        assertEquals(0, game.getCells().countUncoveredPositions());
        assertTrue(game.getCells().isCellFlagged(coordinate));
    }

    @Test
    public void flagCellTest() {
        Set<Coordinate> bombsPositions = new HashSet<>();
        bombsPositions.add(new Coordinate(1, 1));
        Game game = givingABoard(2, bombsPositions);

        final Coordinate coordinate = new Coordinate(0, 0);
        ClickResult clickResult = game.flag(coordinate);
        assertEquals(ClickResult.FLAGGED, clickResult);
        assertEquals(1, game.getCells().getFlaggedPositions().size());
    }

    @Test
    public void removeFlagCellTest() {
        Set<Coordinate> bombsPositions = new HashSet<>();
        bombsPositions.add(new Coordinate(1, 1));
        Game game = givingABoard(2, bombsPositions);

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
        Set<Coordinate> bombsPositions = new HashSet<>();
        bombsPositions.add(new Coordinate(1, 1));
        Game game = givingABoard(2, bombsPositions);

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
        Set<Coordinate> bombsPositions = new HashSet<>();
        bombsPositions.add(new Coordinate(1, 1));
        Game game = givingABoard(2, bombsPositions);

        ClickResult clickResult = game.click(new Coordinate(0, 0));
        assertEquals(ClickResult.EMPTY_CELL, clickResult);

        clickResult = game.click(new Coordinate(0, 1));
        assertEquals(ClickResult.EMPTY_CELL, clickResult);

        clickResult = game.click(new Coordinate(1, 0));
        assertEquals(ClickResult.WIN, clickResult);
    }

    private Game givingABoard(int size, Set<Coordinate> bombsPositions) {
        return new Game(new ObjectId(), size, size, new Bombs(bombsPositions), new Cells(), GameStatus.PLAYING);
    }
}