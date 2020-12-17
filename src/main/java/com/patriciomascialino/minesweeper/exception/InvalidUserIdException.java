package com.patriciomascialino.minesweeper.exception;

public class InvalidUserIdException extends RuntimeException {
    public InvalidUserIdException(String userId) {
        super(String.format("Invalid user id format. Received: %s", userId));
    }
}
