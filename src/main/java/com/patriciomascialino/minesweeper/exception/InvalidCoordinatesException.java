package com.patriciomascialino.minesweeper.exception;

public class InvalidCoordinatesException extends RuntimeException {
    public InvalidCoordinatesException(int x, int y) {
        super(String.format("Coordinates should be greater or equal to zero. Received: (%s, %s)", x, y));
    }
}
