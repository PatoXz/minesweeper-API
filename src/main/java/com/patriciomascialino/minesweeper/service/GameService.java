package com.patriciomascialino.minesweeper.service;

import com.patriciomascialino.minesweeper.api.response.ClickResponse;
import com.patriciomascialino.minesweeper.exception.GameNotFoundException;
import com.patriciomascialino.minesweeper.exception.InvalidGameIdException;
import com.patriciomascialino.minesweeper.exception.InvalidUserIdException;
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
    private final UserService userService;

    @Autowired
    public GameService(GameRepository gameRepository, UserService userService) {
        this.gameRepository = gameRepository;
        this.userService = userService;
    }

    public Game newGame(final int boardHeight, final int boardWidth, final int bombsCount, final String userId) {
        userService.getUser(userId);
        Game game = new Game(boardHeight, boardWidth, bombsCount, new ObjectId(userId));
        gameRepository.save(game);
        return game;
    }

    public Game loadGame(final String gameId, final String userId) {
        if (!ObjectId.isValid(gameId))
            throw new InvalidGameIdException(gameId);
        if (!ObjectId.isValid(userId))
            throw new InvalidUserIdException(userId);

        return gameRepository.findByIdAndUserID(new ObjectId(gameId), new ObjectId(userId))
                .orElseThrow(() -> new GameNotFoundException(gameId, userId));
    }

    public ClickResponse clickCell(final String gameId, final Coordinate coordinate, final String userId) {
        Game game = loadGame(gameId, userId);
        final ClickResult clickResult = game.click(coordinate);
        gameRepository.save(game);
        return new ClickResponse(clickResult, game);
    }

    public ClickResponse flagCell(final String gameId, final Coordinate coordinate, final String userId) {
        Game game = loadGame(gameId, userId);
        final ClickResult clickResult = game.flag(coordinate);
        gameRepository.save(game);
        return new ClickResponse(clickResult, game);
    }
}
