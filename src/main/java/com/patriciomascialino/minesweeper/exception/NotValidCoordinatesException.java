package com.patriciomascialino.minesweeper.exception;

public class NotValidCoordinatesException extends RuntimeException {
    public NotValidCoordinatesException(int x, int y) {
        super(String.format("Coordinates should be greater or equal to zero. Received: (%s, %s)", x, y));
    }
}
