package com.patriciomascialino.minesweeper.service;

import com.patriciomascialino.minesweeper.exception.GameNotFoundException;
import com.patriciomascialino.minesweeper.api.response.ClickResponse;
import com.patriciomascialino.minesweeper.model.ClickResult;
import com.patriciomascialino.minesweeper.model.Coordinate;
import com.patriciomascialino.minesweeper.model.Game;
import com.patriciomascialino.minesweeper.repository.GameRepository;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class GameService {
    private final GameRepository gameRepository;

    @Autowired
    public GameService(GameRepository gameRepository) {
        this.gameRepository = gameRepository;
    }

    public Game newGame(final int boardHeight, final int boardWidth, final int bombsCount) {
        Game game = new Game(boardHeight, boardWidth, bombsCount);
        gameRepository.save(game);
        return game;
    }

    public Game loadGame(final String gameId) {
        return gameRepository.findById(new ObjectId(gameId))
                .orElseThrow(() -> new GameNotFoundException(gameId));
    }

    public ClickResponse clickCell(String gameId, Coordinate coordinate) {
        Game game = loadGame(gameId);
        final ClickResult clickResult = game.click(coordinate);
        gameRepository.save(game);
        return new ClickResponse(clickResult, game);
    }

    public ClickResponse flagCell(String gameId, Coordinate coordinate) {
        Game game = loadGame(gameId);
        final ClickResult clickResult = game.flag(coordinate);
        gameRepository.save(game);
        return new ClickResponse(clickResult, game);
    }
}
