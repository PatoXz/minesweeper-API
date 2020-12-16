package com.patriciomascialino.minesweeper.model;

public enum GameStatus {
    WON,
    PLAYING,
    LOST;

    public boolean isFinished() {
        return this == WON || this == LOST;
    }
}
