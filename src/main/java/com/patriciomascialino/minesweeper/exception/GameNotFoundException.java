package com.patriciomascialino.minesweeper.exception;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class GameNotFoundException extends RuntimeException {
    public GameNotFoundException(String gameId) {
        super(String.format("Game not found. ID %s", gameId));
        log.error(this.getMessage());
    }
}
