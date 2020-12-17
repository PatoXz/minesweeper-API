package com.patriciomascialino.minesweeper.exception;

public class NotEnoughFreeCellsOnBoardException extends RuntimeException {
    public NotEnoughFreeCellsOnBoardException(int cellsCount, int bombsCount) {
        super(String.format("There wasn't enough free cells on the game. It should be at least one. " +
                "Cells on board %s, bombs to set: %s", cellsCount, bombsCount));
    }
}
