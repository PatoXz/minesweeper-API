package com.patriciomascialino.minesweeper.api;

import com.patriciomascialino.minesweeper.api.request.BoardProperties;
import com.patriciomascialino.minesweeper.api.response.ClickResponse;
import com.patriciomascialino.minesweeper.api.response.GameResponse;
import com.patriciomascialino.minesweeper.model.Coordinate;
import com.patriciomascialino.minesweeper.model.Game;
import com.patriciomascialino.minesweeper.service.GameService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/minesweeper")
@Slf4j
public class MineSweeperController {
    private final GameService gameService;

    @Autowired
    public MineSweeperController(GameService gameService) {
        this.gameService = gameService;
    }

    @PostMapping
    public ResponseEntity<GameResponse> newGame(
            @RequestBody final BoardProperties boardProperties) {
        log.info("New game request {}", boardProperties);
        final Game game = gameService.newGame(
                boardProperties.getBoardHeight(),
                boardProperties.getBoardWidth(),
                boardProperties.getBombsCount());
        return ResponseEntity.ok(GameResponse.of(game));
    }

    @GetMapping("/{gameId}")
    public ResponseEntity<GameResponse> loadGame(
            @PathVariable("gameId") final String gameId) {
        log.info("Load game request. GameId: {}", gameId);
        final Game game = gameService.loadGame(gameId);
        return ResponseEntity.ok(GameResponse.of(game));
    }

    @PostMapping("/{gameId}/click")
    public ResponseEntity<ClickResponse> clickCell(
            @PathVariable("gameId") final String gameId,
            @RequestBody final Coordinate coordinate) {
        log.info("Load game request. GameId: {}", gameId);
        final ClickResponse clickResponse = gameService.clickCell(gameId, coordinate);
        return ResponseEntity.ok(clickResponse);
    }

    @PostMapping("/{gameId}/flag")
    public ResponseEntity<ClickResponse> flagCell(
            @PathVariable("gameId") final String gameId,
            @RequestBody final Coordinate coordinate) {
        log.info("Load game request. GameId: {}", gameId);
        final ClickResponse clickResponse = gameService.flagCell(gameId, coordinate);
        return ResponseEntity.ok(clickResponse);
    }
}
