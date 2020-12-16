package com.patriciomascialino.minesweeper.api.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.patriciomascialino.minesweeper.model.ClickResult;
import com.patriciomascialino.minesweeper.model.Game;
import lombok.Getter;

@Getter
public class ClickResponse {
    @JsonProperty("click_result")
    private final ClickResult clickResult;
    @JsonProperty("game")
    private final GameResponse gameResponse;

    public ClickResponse(final ClickResult clickResult,
                         final Game game) {
        this.clickResult = clickResult;
        this.gameResponse = GameResponse.of(game);
    }
}
