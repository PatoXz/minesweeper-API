package com.patriciomascialino.minesweeper.api.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.patriciomascialino.minesweeper.api.request.BoardProperties;
import com.patriciomascialino.minesweeper.model.Game;
import com.patriciomascialino.minesweeper.model.GameStatus;
import lombok.Getter;

import java.time.ZonedDateTime;

@Getter
@JsonIgnoreProperties(ignoreUnknown = true)
public class GameResponse {
    @JsonProperty("board_properties")
    private BoardProperties boardProperties;
    @JsonProperty("user_id")
    private String userId;
    @JsonProperty("game_id")
    private String gameId;
    @JsonProperty("cells")
    private CellsResponse cells;
    @JsonProperty("game_status")
    private GameStatus gameStatus;
    @JsonProperty("game_started_at")
    private ZonedDateTime gameStartedAt;
    @JsonProperty("game_finished_at")
    private ZonedDateTime gameFinishedAt;

    public static GameResponse of(Game game) {
        GameResponse gameResponse = new GameResponse();
        gameResponse.userId = game.getUserID().toString();
        gameResponse.gameId = game.getId().toString();
        gameResponse.boardProperties =
                new BoardProperties(game.getBoardHeight(), game.getBoardWidth(), game.getBombsCount());
        gameResponse.cells = CellsResponse.of(game);
        gameResponse.gameStatus = game.getGameStatus();
        gameResponse.gameStartedAt = game.getGameStartedAt();
        gameResponse.gameFinishedAt = game.getGameFinishedAt();
        return gameResponse;
    }
}
