package com.patriciomascialino.minesweeper.api;

import com.patriciomascialino.minesweeper.api.request.BoardProperties;
import com.patriciomascialino.minesweeper.api.request.ClickRequest;
import com.patriciomascialino.minesweeper.api.response.ClickResponse;
import com.patriciomascialino.minesweeper.api.response.GameResponse;
import com.patriciomascialino.minesweeper.model.Game;
import com.patriciomascialino.minesweeper.service.GameService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;

@RestController
@RequestMapping("/minesweeper")
@Slf4j
public class MineSweeperController {
    private final GameService gameService;

    @Autowired
    public MineSweeperController(GameService gameService) {
        this.gameService = gameService;
    }

    @ApiOperation(value = "Start a new game with the properties sent", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiResponses({@ApiResponse(code = 200, message = "Game created", response = GameResponse.class)})
    @PostMapping
    public ResponseEntity<GameResponse> newGame(
            @RequestBody @Valid final BoardProperties boardProperties) {
        log.info("New game request {}", boardProperties);
        final Game game = gameService.newGame(
                boardProperties.getBoardHeight(),
                boardProperties.getBoardWidth(),
                boardProperties.getBombsCount());
        return ResponseEntity.ok(GameResponse.of(game));
    }

    @ApiOperation(value = "Load an existing game by its ID", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiResponses({
        @ApiResponse(code = 200, message = "Game found", response = GameResponse.class),
        @ApiResponse(code = 404, message = "Game not found", response = String.class)
    })
    @GetMapping("/{gameId}")
    public ResponseEntity<GameResponse> loadGame(
            @PathVariable("gameId") @ApiParam(value = "The game to look for") @Valid @NotBlank
            final String gameId) {
        log.info("Load game request. GameId: {}", gameId);
        final Game game = gameService.loadGame(gameId);
        return ResponseEntity.ok(GameResponse.of(game));
    }

    @ApiOperation(value = "Uncover a position", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiResponses({
            @ApiResponse(
                    code = 200,
                    message = "Returns all the game status after uncover the cell",
                    response = ClickResponse.class),
            @ApiResponse(
                    code = 400,
                    message = "Click request can not have negative values",
                    response = String.class),
            @ApiResponse(
                    code = 404,
                    message = "Game not found",
                    response = String.class)
    })

    @PostMapping("/{gameId}/click")
    public ResponseEntity<ClickResponse> clickCell(
            @PathVariable("gameId") @ApiParam(value = "The game to perform the action") @Valid @NotBlank
            final String gameId,
            @RequestBody @Valid final ClickRequest clickRequest) {
        log.info("ClickCell request. GameId: {}, clickRequest: {}", gameId, clickRequest);
        final ClickResponse clickResponse = gameService.clickCell(gameId, clickRequest.toCoordinate());
        return ResponseEntity.ok(clickResponse);
    }

    @ApiOperation(value = "Mark a position as flagged", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiResponses({
            @ApiResponse(
                    code = 200,
                    message = "Returns all the game status after mark as flagged the cell",
                    response = ClickResponse.class),
            @ApiResponse(
                    code = 400,
                    message = "Click request can not have negative values",
                    response = String.class),
            @ApiResponse(
                    code = 404,
                    message = "Game not found",
                    response = String.class)
    })
    @PostMapping("/{gameId}/flag")
    public ResponseEntity<ClickResponse> flagCell(
            @PathVariable("gameId") @ApiParam(value = "The game to perform the action") @Valid @NotBlank
            final String gameId,
            @RequestBody @Valid final ClickRequest clickRequest) {
        log.info("FlagCell request. GameId: {}, clickRequest: {}", gameId, clickRequest);
        final ClickResponse clickResponse = gameService.flagCell(gameId, clickRequest.toCoordinate());
        return ResponseEntity.ok(clickResponse);
    }
}
