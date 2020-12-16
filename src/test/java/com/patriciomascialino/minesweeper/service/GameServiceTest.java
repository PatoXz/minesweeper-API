package com.patriciomascialino.minesweeper.service;

import com.patriciomascialino.minesweeper.api.response.ClickResponse;
import com.patriciomascialino.minesweeper.exception.GameNotFoundException;
import com.patriciomascialino.minesweeper.model.*;
import com.patriciomascialino.minesweeper.repository.GameRepository;
import org.bson.types.ObjectId;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

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

    @BeforeEach
    public void beforeEach() {
        this.gameRepository = mock(GameRepository.class);
        this.gameService = new GameService(gameRepository);
    }

    @Test
    public void newGameTest() {
        Game game = new Game(2, 2, 1);
        when(gameRepository.save(any())).thenReturn(game);
        Game gameOfService = gameService.newGame(2, 2, 1);
        assertEquals(game.getBoardHeight(), gameOfService.getBoardHeight());
        assertEquals(game.getBoardWidth(), gameOfService.getBoardWidth());
        assertEquals(game.getBombsCount(), gameOfService.getBombsCount());
    }

    @Test
    public void throwErrorWhenGameNotFoundTest() {
        when(gameRepository.findById(any())).thenReturn(Optional.empty());
        assertThrows(GameNotFoundException.class, () -> gameService.loadGame(new ObjectId().toString()));
    }

    @Test
    public void clickCellTest() {
        Set<Coordinate> bombsPositions = new HashSet<>();
        bombsPositions.add(new Coordinate(1, 1));
        final ObjectId gameId = new ObjectId();
        Game game = new Game(gameId, 2, 2, new Bombs(bombsPositions), new Cells(), GameStatus.PLAYING);
        when(gameRepository.findById(gameId)).thenReturn(Optional.of(game));
        when(gameRepository.save(any())).thenReturn(game);
        final Coordinate clickCoordinate = new Coordinate(0, 0);
        ClickResponse clickResponse = gameService.clickCell(gameId.toString(), clickCoordinate);
        assertTrue(clickResponse.getGameResponse().getCells().isCellUncovered(clickCoordinate));
        assertEquals(1, clickResponse.getGameResponse().getCells().countUncoveredPositions());
    }

    @Test
    public void flagCellTest() {
        Set<Coordinate> bombsPositions = new HashSet<>();
        bombsPositions.add(new Coordinate(1, 1));
        final ObjectId gameId = new ObjectId();
        Game game = new Game(gameId, 2, 2, new Bombs(bombsPositions), new Cells(), GameStatus.PLAYING);
        when(gameRepository.findById(gameId)).thenReturn(Optional.of(game));
        when(gameRepository.save(any())).thenReturn(game);
        final Coordinate clickCoordinate = new Coordinate(0, 0);
        ClickResponse clickResponse = gameService.flagCell(gameId.toString(), clickCoordinate);
        assertTrue(clickResponse.getGameResponse().getCells().isCellFlagged(clickCoordinate));
        assertEquals(1, clickResponse.getGameResponse().getCells().getFlaggedPositions().size());
    }
}
