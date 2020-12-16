package com.patriciomascialino.minesweeper.exception;

public class InvalidGameIdException extends RuntimeException {
    public InvalidGameIdException(String gameId) {
        super(String.format("Invalid game id format. Received: %s", gameId));
    }
}
