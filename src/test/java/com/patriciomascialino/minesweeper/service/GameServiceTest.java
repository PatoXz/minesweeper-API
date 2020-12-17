package com.patriciomascialino.minesweeper.service;

import com.patriciomascialino.minesweeper.api.response.ClickResponse;
import com.patriciomascialino.minesweeper.exception.GameNotFoundException;
import com.patriciomascialino.minesweeper.exception.InvalidGameIdException;
import com.patriciomascialino.minesweeper.exception.InvalidUserIdException;
import com.patriciomascialino.minesweeper.model.*;
import com.patriciomascialino.minesweeper.repository.GameRepository;
import org.bson.types.ObjectId;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class GameServiceTest {
    GameRepository gameRepository;
    GameService gameService;
    UserService userService;

    @BeforeEach
    public void beforeEach() {
        this.gameRepository = mock(GameRepository.class);
        this.userService = mock(UserService.class);
        this.gameService = new GameService(gameRepository, userService);
    }

    @Test
    public void newGameTest() {
        ObjectId userId = new ObjectId();
        Game game = new Game(2, 2, 1, userId);
        when(gameRepository.save(any())).thenReturn(game);
        Game gameOfService = gameService.newGame(2, 2, 1, userId.toString());
        assertEquals(game.getBoardHeight(), gameOfService.getBoardHeight());
        assertEquals(game.getBoardWidth(), gameOfService.getBoardWidth());
        assertEquals(game.getBombsCount(), gameOfService.getBombsCount());
    }

    @Test
    public void throwErrorWhenGameNotFoundTest() {
        when(gameRepository.findByIdAndUserID(any(), any())).thenReturn(Optional.empty());
        assertThrows(GameNotFoundException.class,
                () -> gameService.loadGame(new ObjectId().toString(), new ObjectId().toString()));
    }

    @Test
    public void loadGameTest() {
        ObjectId gameId = new ObjectId();
        ObjectId userId = new ObjectId();
        Game game = new Game(2, 2, 1, userId);
        when(gameRepository.findByIdAndUserID(gameId, userId)).thenReturn(Optional.of(game));
        Game gameOfService = gameService.loadGame(gameId.toString(), userId.toString());
        assertEquals(game.getBoardHeight(), gameOfService.getBoardHeight());
        assertEquals(game.getBoardWidth(), gameOfService.getBoardWidth());
        assertEquals(game.getBombsCount(), gameOfService.getBombsCount());
    }

    @Test
    public void clickCellTest() {
        ObjectId userId = new ObjectId();
        Set<Coordinate> bombsPositions = new HashSet<>();
        bombsPositions.add(new Coordinate(1, 1));
        final ObjectId gameId = new ObjectId();
        Game game = new Game(gameId, 2, 2, new Bombs(bombsPositions),
                new Cells(), GameStatus.PLAYING, ZonedDateTime.now(ZoneOffset.UTC).toInstant(), userId);
        when(gameRepository.findByIdAndUserID(gameId, userId)).thenReturn(Optional.of(game));
        when(gameRepository.save(any())).thenReturn(game);
        final Coordinate clickCoordinate = new Coordinate(0, 0);
        ClickResponse clickResponse = gameService.clickCell(gameId.toString(), clickCoordinate, userId.toString());
        assertTrue(clickResponse.getGameResponse().getCells().getUncoveredPositions().stream()
                .anyMatch(uncoveredPosition -> uncoveredPosition.equals(clickCoordinate)));
        assertEquals(1, clickResponse.getGameResponse().getCells().getUncoveredPositions().size());
    }

    @Test
    public void flagCellTest() {
        ObjectId userId = new ObjectId();
        Set<Coordinate> bombsPositions = new HashSet<>();
        bombsPositions.add(new Coordinate(1, 1));
        final ObjectId gameId = new ObjectId();
        Game game = new Game(gameId, 2, 2, new Bombs(bombsPositions),
                new Cells(), GameStatus.PLAYING, ZonedDateTime.now(ZoneOffset.UTC).toInstant(), userId);
        when(gameRepository.findByIdAndUserID(gameId, userId)).thenReturn(Optional.of(game));
        when(gameRepository.save(any())).thenReturn(game);
        final Coordinate clickCoordinate = new Coordinate(0, 0);
        ClickResponse clickResponse = gameService.flagCell(gameId.toString(), clickCoordinate, userId.toString());
        assertTrue(clickResponse.getGameResponse().getCells().getFlaggedPositions().stream()
                .anyMatch(flaggedPosition -> flaggedPosition.equals(clickCoordinate)));
        assertEquals(1, clickResponse.getGameResponse().getCells().getFlaggedPositions().size());
    }

    @Test
    public void throwExceptionWhenInvalidGameId() {
        final InvalidGameIdException exception = assertThrows(InvalidGameIdException.class,
                () -> gameService.loadGame("invalidGameId", new ObjectId().toString()));
        assertEquals("Invalid game id format. Received: invalidGameId", exception.getMessage());
    }

    @Test
    public void throwExceptionWhenInvalidUserId() {
        final InvalidUserIdException exception = assertThrows(InvalidUserIdException.class,
                () -> gameService.loadGame(new ObjectId().toString(), "invalidUserId"));
        assertEquals("Invalid user id format. Received: invalidUserId", exception.getMessage());
    }
}
