package com.patriciomascialino.minesweeper.exception;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class UserNotFoundException extends RuntimeException {
    public UserNotFoundException(String userId) {
        super(String.format("User not found. ID %s", userId));
        log.error(this.getMessage());
    }
}
