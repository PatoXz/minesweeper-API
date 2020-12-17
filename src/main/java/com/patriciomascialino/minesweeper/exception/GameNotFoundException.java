package com.patriciomascialino.minesweeper.exception;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class GameNotFoundException extends RuntimeException {
    public GameNotFoundException(String gameId, String userId) {
        super(String.format("Game %s not found for user %s", gameId, userId));
        log.error(this.getMessage());
    }
}
