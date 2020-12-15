package com.patriciomascialino.minesweeper.exception;

public class BombFoundedException extends Exception {
    public BombFoundedException() {
        super("Bomb founded");
    }
}
