package com.patriciomascialino.minesweeper.api.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.patriciomascialino.minesweeper.model.Coordinate;
import com.patriciomascialino.minesweeper.model.Game;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Set;

@AllArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@JsonIgnoreProperties(ignoreUnknown = true)
public class CellsResponse {
    @JsonProperty("uncovered_positions")
    private final Set<Coordinate> uncoveredPositions;
    @JsonProperty("flagged_positions")
    private final Set<Coordinate> flaggedPositions;

    public static CellsResponse of(Game game) {
        return new CellsResponse(game.getCells().getUncoveredPositions(), game.getCells().getFlaggedPositions());
    }
}
