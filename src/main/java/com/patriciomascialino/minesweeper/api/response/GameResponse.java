package com.patriciomascialino.minesweeper.api.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.patriciomascialino.minesweeper.api.request.BoardProperties;
import com.patriciomascialino.minesweeper.model.Cells;
import com.patriciomascialino.minesweeper.model.Game;
import com.patriciomascialino.minesweeper.model.GameStatus;
import lombok.Getter;

@Getter
public class GameResponse {
    @JsonProperty("board_properties")
    private BoardProperties boardProperties;
    @JsonProperty("game_id")
    private String gameId;
    @JsonProperty("cells")
    private CellsResponse cells;
    @JsonProperty("game_status")
    private GameStatus gameStatus;

    public static GameResponse of(Game game) {
        GameResponse gameResponse = new GameResponse();
        gameResponse.gameId = game.getGameId().toString();
        gameResponse.boardProperties =
                new BoardProperties(game.getBoardHeight(), game.getBoardWidth(), game.getBombsCount());
        gameResponse.cells = CellsResponse.of(game);
        gameResponse.gameStatus = game.getGameStatus();
        return gameResponse;
    }
}
