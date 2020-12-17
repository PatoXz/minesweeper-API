package com.patriciomascialino.minesweeper.api.response;

import com.patriciomascialino.minesweeper.model.*;
import org.bson.types.ObjectId;
import org.junit.jupiter.api.Test;

import java.time.ZonedDateTime;
import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

public class ClickResponseTest {
    @Test
    public void clickResponseTest() {
        final ObjectId gameId = new ObjectId();
        Game game = givingAGameWithOneBombOneClickAndOneFlaggedCell(gameId);

        ClickResponse clickResponse = new ClickResponse(ClickResult.EMPTY_CELL, game);

        assertEquals(ClickResult.EMPTY_CELL, clickResponse.getClickResult());
        assertEquals(game.getBoardHeight(), clickResponse.getGameResponse().getBoardProperties().getBoardHeight());
        assertEquals(game.getBoardWidth(), clickResponse.getGameResponse().getBoardProperties().getBoardWidth());
        assertEquals(game.getBombsCount(), clickResponse.getGameResponse().getBoardProperties().getBombsCount());
        assertEquals(gameId.toString(), clickResponse.getGameResponse().getGameId());
        assertEquals(1, clickResponse.getGameResponse().getCells().getUncoveredPositions().size());
        assertTrue(clickResponse.getGameResponse().getCells().getUncoveredPositions().stream()
                .anyMatch(x -> x.equals(new Coordinate(0, 0))));
        assertEquals(1, clickResponse.getGameResponse().getCells().getFlaggedPositions().size());
        assertTrue(clickResponse.getGameResponse().getCells().getFlaggedPositions().stream()
                .anyMatch(x -> x.equals(new Coordinate(1, 0))));
        assertEquals(GameStatus.PLAYING, clickResponse.getGameResponse().getGameStatus());
        assertEquals(ZonedDateTime.parse("2020-12-16T00:00:00Z"), clickResponse.getGameResponse().getGameStartedAt());
        assertNull(clickResponse.getGameResponse().getGameFinishedAt());
    }

    @Test
    public void gameFinishedResponseTest() {
        final ObjectId gameId = new ObjectId();
        Game game = givingAGameWithOneBombOneClickAndOneFlaggedCell(gameId);
        game.click(new Coordinate(1, 1));

        ClickResponse clickResponse = new ClickResponse(ClickResult.GAME_ALREADY_FINISHED, game);

        assertEquals(ClickResult.GAME_ALREADY_FINISHED, clickResponse.getClickResult());
        assertEquals(game.getBoardHeight(), clickResponse.getGameResponse().getBoardProperties().getBoardHeight());
        assertEquals(game.getBoardWidth(), clickResponse.getGameResponse().getBoardProperties().getBoardWidth());
        assertEquals(game.getBombsCount(), clickResponse.getGameResponse().getBoardProperties().getBombsCount());
        assertEquals(gameId.toString(), clickResponse.getGameResponse().getGameId());
        assertEquals(1, clickResponse.getGameResponse().getCells().getUncoveredPositions().size());
        assertTrue(clickResponse.getGameResponse().getCells().getUncoveredPositions().stream()
                .anyMatch(x -> x.equals(new Coordinate(0, 0))));
        assertEquals(1, clickResponse.getGameResponse().getCells().getFlaggedPositions().size());
        assertTrue(clickResponse.getGameResponse().getCells().getFlaggedPositions().stream()
                .anyMatch(x -> x.equals(new Coordinate(1, 0))));
        assertEquals(GameStatus.LOST, clickResponse.getGameResponse().getGameStatus());
        assertEquals(ZonedDateTime.parse("2020-12-16T00:00:00Z"), clickResponse.getGameResponse().getGameStartedAt());
        assertNotNull(clickResponse.getGameResponse().getGameFinishedAt());
    }

    private Game givingAGameWithOneBombOneClickAndOneFlaggedCell(ObjectId gameId) {
        Set<Coordinate> bombsPositions = new HashSet<>();
        bombsPositions.add(new Coordinate(1, 1));

        Game game = new Game(gameId, 2, 3,
                new Bombs(bombsPositions), new Cells(), GameStatus.PLAYING,
                ZonedDateTime.parse("2020-12-16T00:00:00Z").toInstant(), new ObjectId());
        game.click(new Coordinate(0, 0));
        game.flag(new Coordinate(1, 0));
        return game;
    }
}
