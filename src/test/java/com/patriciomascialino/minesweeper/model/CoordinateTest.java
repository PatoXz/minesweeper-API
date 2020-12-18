package com.patriciomascialino.minesweeper.model;

import com.patriciomascialino.minesweeper.exception.InvalidCoordinatesException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class CoordinateTest {
    @Test
    public void createCoordinateTest() {
        Coordinate coordinate = new Coordinate(1, 2);
        assertEquals(1, coordinate.getX());
        assertEquals(2, coordinate.getY());
    }
    @Test
    public void getRightCellFromCoordinateTest() {
        Coordinate coordinate = new Coordinate(2, 2);
        Coordinate rightCell = coordinate.rightCell();
        assertEquals(3, rightCell.getX());
        assertEquals(2, rightCell.getY());
    }
    @Test
    public void getLeftCellFromCoordinateTest() {
        Coordinate coordinate = new Coordinate(2, 2);
        Coordinate leftCell = coordinate.leftCell();
        assertEquals(1, leftCell.getX());
        assertEquals(2, leftCell.getY());
    }
    @Test
    public void getAboveCellFromCoordinateTest() {
        Coordinate coordinate = new Coordinate(2, 2);
        Coordinate aboveCell = coordinate.aboveCell();
        assertEquals(2, aboveCell.getX());
        assertEquals(1, aboveCell.getY());
    }
    @Test
    public void getBelowCellFromCoordinateTest() {
        Coordinate coordinate = new Coordinate(2, 2);
        Coordinate belowCell = coordinate.belowCell();
        assertEquals(2, belowCell.getX());
        assertEquals(3, belowCell.getY());
    }
    @Test
    public void toStringCoordinateTest() {
        Coordinate coordinate = new Coordinate(2, 2);
        assertEquals("(2, 2)", coordinate.toString());
    }
    @Test
    public void coordinateThrowsExceptionWhenXLessThanZeroTest() {
        final InvalidCoordinatesException invalidCoordinatesException =
                Assertions.assertThrows(InvalidCoordinatesException.class,
                        () -> new Coordinate(-1, 0));
        assertEquals("Coordinates should be greater or equal to zero. Received: (-1, 0)",
                invalidCoordinatesException.getMessage());
    }

    @Test
    public void coordinateThrowsExceptionWhenYLessThanZeroTest() {
        final InvalidCoordinatesException invalidCoordinatesException =
                Assertions.assertThrows(InvalidCoordinatesException.class,
                        () -> new Coordinate(0, -1));
        assertEquals("Coordinates should be greater or equal to zero. Received: (0, -1)",
                invalidCoordinatesException.getMessage());
    }

    @Test
    public void coordinateThrowsExceptionWhenXAndYLessThanZeroTest() {
        final InvalidCoordinatesException invalidCoordinatesException =
                Assertions.assertThrows(InvalidCoordinatesException.class,
                        () -> new Coordinate(-1, -1));
        assertEquals("Coordinates should be greater or equal to zero. Received: (-1, -1)",
                invalidCoordinatesException.getMessage());
    }
}
