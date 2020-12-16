package com.patriciomascialino.minesweeper.model;

import com.patriciomascialino.minesweeper.exception.NotValidCoordinatesException;
import lombok.Getter;
import javax.validation.constraints.Min;

import java.util.Objects;

@Getter
public class Coordinate {
    private final int x;
    private final int y;

    public Coordinate(final int x, final int y) {
        if (x < 0 || y < 0) {
            throw new NotValidCoordinatesException(x, y);
        }
        this.x = x;
        this.y = y;
    }

    public Coordinate aboveCell() {
        return new Coordinate(this.x, this.y - 1);
    }

    public Coordinate belowCell() {
        return new Coordinate(this.x, this.y + 1);
    }

    public Coordinate rightCell() {
        return new Coordinate(this.x + 1, this.y);
    }

    public Coordinate leftCell() {
        return new Coordinate(this.x - 1, this.y);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Coordinate that = (Coordinate) o;
        return x == that.x &&
                y == that.y;
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y);
    }

    @Override
    public String toString() {
        return "(" + x + ", " + y + ")";
    }
}
