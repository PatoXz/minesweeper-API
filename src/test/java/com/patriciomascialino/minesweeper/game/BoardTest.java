package com.patriciomascialino.minesweeper.game;

import com.patriciomascialino.minesweeper.utils.Coordinate;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class BoardTest {
    @Test
    public void bombHitTest() {
        Board board = new Board(10, 10, 1);
        final Coordinate first = board.getBombsPositions().stream().findFirst().orElseThrow(RuntimeException::new);

        ClickResponse click = board.click(first.getX(), first.getY());
        assertEquals(ClickResult.BOMB, click.getClickResult());
    }

    @Test
    public void clickCellTwiceTest() {
        Set<Coordinate> bombsPositions = new HashSet<>();
        bombsPositions.add(new Coordinate(0, 0));
        Board board = new Board(2, 2, bombsPositions, new HashSet<>());

        ClickResponse click = board.click(1, 1);
        assertEquals(ClickResult.EMPTY_CELL, click.getClickResult());

        click = board.click(1, 1);
        assertEquals(ClickResult.ALREADY_UNCOVERED, click.getClickResult());

        assertEquals(1, click.getUncoveredPositions().size());
        Assertions.assertTrue(click.getUncoveredPositions().stream()
                .anyMatch(uncoveredPosition -> uncoveredPosition.getX() == 1 && uncoveredPosition.getY() == 1));
    }

    @Test
    public void cellWithAdjacentBombTest() {
        Set<Coordinate> bombsPositions = new HashSet<>();
        bombsPositions.add(new Coordinate(1, 1));
        Board board = new Board(10, 10, bombsPositions, new HashSet<>());

        ClickResponse click = board.click(2, 2);
        assertEquals(ClickResult.EMPTY_CELL, click.getClickResult());

        assertEquals(1, click.getUncoveredPositions().size());
        Assertions.assertTrue(click.getUncoveredPositions().stream()
                .anyMatch(uncoveredPosition -> uncoveredPosition.getX() == 2 && uncoveredPosition.getY() == 2));
    }

    @Test
    public void cellWithNoAdjacentBombAndOneBombTest() {
        Set<Coordinate> bombsPositions = new HashSet<>();
        bombsPositions.add(new Coordinate(1, 1));
        Board board = new Board(5, 5, bombsPositions, new HashSet<>());

        ClickResponse click = board.click(3, 3);
        assertEquals(ClickResult.EMPTY_CELL, click.getClickResult());

        assertEquals(21, click.getUncoveredPositions().size());
        Assertions.assertTrue(click.getUncoveredPositions().stream()
                .anyMatch(uncoveredPosition -> uncoveredPosition.getX() == 2 && uncoveredPosition.getY() == 2));
    }

    @Test
    public void cellWithNoAdjacentBombAndTwoBombsTest() {
        Set<Coordinate> bombsPositions = new HashSet<>();
        bombsPositions.add(new Coordinate(1, 1));
        bombsPositions.add(new Coordinate(4, 4));
        Board board = new Board(5, 5, bombsPositions, new HashSet<>());

        ClickResponse click = board.click(0, 4);
        assertEquals(ClickResult.EMPTY_CELL, click.getClickResult());

        assertEquals(20, click.getUncoveredPositions().size());
    }

    @Test
    public void cellWithNoAdjacentBombAndTwoBombs2Test() {
        Set<Coordinate> bombsPositions = new HashSet<>();
        bombsPositions.add(new Coordinate(1, 1));
        bombsPositions.add(new Coordinate(3, 3));
        Board board = new Board(5, 5, bombsPositions, new HashSet<>());

        ClickResponse click = board.click(0, 4);
        assertEquals(ClickResult.EMPTY_CELL, click.getClickResult());

        assertEquals(9, click.getUncoveredPositions().size());
    }

    @Test
    public void cellWithNoAdjacentBombAndTwoBombs3Test() {
        Set<Coordinate> bombsPositions = new HashSet<>();
        bombsPositions.add(new Coordinate(1, 1));
        bombsPositions.add(new Coordinate(3, 1));
        Board board = new Board(5, 5, bombsPositions, new HashSet<>());

        ClickResponse click = board.click(0, 4);
        assertEquals(ClickResult.EMPTY_CELL, click.getClickResult());

        assertEquals(15, click.getUncoveredPositions().size());
    }

    @Test
    public void cellWithNoAdjacentBombAndThreeBombsTest() {
        Set<Coordinate> bombsPositions = new HashSet<>();
        bombsPositions.add(new Coordinate(1, 1));
        bombsPositions.add(new Coordinate(3, 1));
        bombsPositions.add(new Coordinate(2, 3));
        Board board = new Board(5, 5, bombsPositions, new HashSet<>());

        ClickResponse click = board.click(0, 4);
        assertEquals(ClickResult.EMPTY_CELL, click.getClickResult());

        assertEquals(6, click.getUncoveredPositions().size());
    }
}
