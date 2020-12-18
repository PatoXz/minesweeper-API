package com.patriciomascialino.minesweeper.api.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.patriciomascialino.minesweeper.model.ClickResult;
import com.patriciomascialino.minesweeper.model.Game;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@JsonIgnoreProperties(ignoreUnknown = true)
@NoArgsConstructor
@Setter
public class ClickResponse {
    @JsonProperty("click_result")
    private ClickResult clickResult;
    @JsonProperty("game")
    private GameResponse gameResponse;

    public ClickResponse(final ClickResult clickResult,
                         final Game game) {
        this.clickResult = clickResult;
        this.gameResponse = GameResponse.of(game);
    }
}
