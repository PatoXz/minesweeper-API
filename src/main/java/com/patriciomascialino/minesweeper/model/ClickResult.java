package com.patriciomascialino.minesweeper.model;

public enum ClickResult {
    EMPTY_CELL,
    ALREADY_UNCOVERED,
    BOMB,
    FLAGGED,
    UNFLAGGED,
    WIN,
    GAME_ALREADY_FINISHED
}
