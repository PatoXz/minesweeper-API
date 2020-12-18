package com.patriciomascialino.minesweeper.api;

import com.patriciomascialino.minesweeper.api.response.ClickResponse;
import com.patriciomascialino.minesweeper.api.response.ErrorResponse;
import com.patriciomascialino.minesweeper.api.response.GameResponse;
import com.patriciomascialino.minesweeper.api.response.UserResponse;
import com.patriciomascialino.minesweeper.model.ClickResult;
import io.restassured.http.ContentType;
import org.bson.types.ObjectId;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.test.context.ActiveProfiles;

import java.util.HashMap;
import java.util.Map;

import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.*;

@ActiveProfiles(profiles = "test")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class MineSweeperControllerTest {
    @LocalServerPort
    private int port;

    @Test
    public void newGameAPITest() {
        final GameResponse gameResponse = createGame();
        assertEquals(2, gameResponse.getBoardProperties().getBoardHeight());
        assertEquals(3, gameResponse.getBoardProperties().getBoardWidth());
        assertEquals(1, gameResponse.getBoardProperties().getBombsCount());
    }

    @Test
    public void tryToCreateGameWithoutEnoughFreeCellsAPITest() {
        final UserResponse user = createUser();
        Map<String, Object> jsonAsMap = new HashMap<>();
        jsonAsMap.put("board_height", "2");
        jsonAsMap.put("board_width", "2");
        jsonAsMap.put("bombs_count", "5");
        final ErrorResponse errorResponse = given().port(port)
                .contentType(ContentType.JSON)
                .header("user_id", user.getId())
                .body(jsonAsMap)
                .post("/minesweeper")
                .body().as(ErrorResponse.class);
        assertEquals("There wasn't enough free cells on the game. It should be at least one. " +
                "Cells on board 4, bombs to set: 5", errorResponse.getErrorMessage());
    }

    @Test
    public void tryToCreateGameWithNonExistentUserAPITest() {
        Map<String, Object> jsonAsMap = new HashMap<>();
        jsonAsMap.put("board_height", "2");
        jsonAsMap.put("board_width", "2");
        jsonAsMap.put("bombs_count", "5");
        final ObjectId randomUserId = new ObjectId();
        final ErrorResponse errorResponse = given().port(port)
                .contentType(ContentType.JSON)
                .header("user_id", randomUserId.toString())
                .body(jsonAsMap)
                .post("/minesweeper")
                .body().as(ErrorResponse.class);
        assertEquals("User not found. ID " + randomUserId, errorResponse.getErrorMessage());
    }

    @Test
    public void getGameAPITest() {
        final GameResponse gameResponseCreate = createGame();
        final GameResponse gameResponseGet = given().port(port)
                .contentType(ContentType.JSON)
                .header("user_id", gameResponseCreate.getUserId())
                .get("/minesweeper/" + gameResponseCreate.getGameId())
                .body().as(GameResponse.class);
        assertEquals(gameResponseCreate.getGameId(), gameResponseGet.getGameId());
        assertEquals(gameResponseCreate.getUserId(), gameResponseGet.getUserId());
        assertEquals(gameResponseCreate.getGameStatus(), gameResponseGet.getGameStatus());
        assertEquals(gameResponseCreate.getGameStartedAt().toString().substring(0,23),
                gameResponseGet.getGameStartedAt().toString().substring(0,23));
        assertNull(gameResponseGet.getGameFinishedAt());
    }

    @Test
    public void tryToGetGameDifferentUserAPITest() {
        final GameResponse gameResponseCreate = createGame();
        final ObjectId randomUserId = new ObjectId();
        final ErrorResponse errorResponse = given().port(port)
                .contentType(ContentType.JSON)
                .header("user_id", randomUserId.toString())
                .get("/minesweeper/" + gameResponseCreate.getGameId())
                .body().as(ErrorResponse.class);
        assertNotNull(gameResponseCreate.getGameId());
        assertEquals(String.format("Game %s not found for user %s", gameResponseCreate.getGameId(), randomUserId),
                errorResponse.getErrorMessage());
    }

    @Test
    public void tryToGetGameWithInvalidGameIdAPITest() {
        final ObjectId randomUserId = new ObjectId();
        final ErrorResponse errorResponse = given().port(port)
                .contentType(ContentType.JSON)
                .header("user_id", randomUserId)
                .get("/minesweeper/invalidId")
                .body().as(ErrorResponse.class);
        assertEquals("Invalid game id format. Received: invalidId", errorResponse.getErrorMessage());
    }

    @Test
    public void tryToGetGameWithInvalidUserIdAPITest() {
        final ObjectId randomGameId = new ObjectId();
        final ErrorResponse errorResponse = given().port(port)
                .contentType(ContentType.JSON)
                .header("user_id", "invalidId")
                .get("/minesweeper/" + randomGameId.toString())
                .body().as(ErrorResponse.class);
        assertEquals("Invalid user id format. Received: invalidId", errorResponse.getErrorMessage());
    }

    @Test
    public void clickAPITest() {
        final GameResponse gameResponseCreate = createGame();
        final ClickResponse clickResponse = given().port(port)
                .contentType(ContentType.JSON)
                .header("user_id", gameResponseCreate.getUserId())
                .body("{\"x\": 1, \"y\": 1}")
                .post("/minesweeper/" + gameResponseCreate.getGameId() + "/click")
                .body().as(ClickResponse.class);
        assertEquals(gameResponseCreate.getGameId(), clickResponse.getGameResponse().getGameId());
    }

    @Test
    public void tryToClickWithInvalidXCoordinateAPITest() {
        final ObjectId randomGameId = new ObjectId();
        final ObjectId randomUserId = new ObjectId();
        final ErrorResponse errorResponse = given().port(port)
                .contentType(ContentType.JSON)
                .header("user_id", randomUserId)
                .body("{\"x\": -1, \"y\": 1}")
                .post("/minesweeper/" + randomGameId + "/click")
                .body().as(ErrorResponse.class);
        assertEquals("X must be equal or greater than 0", errorResponse.getErrorMessage());
    }

    @Test
    public void tryToClickWithInvalidYCoordinateAPITest() {
        final ObjectId randomGameId = new ObjectId();
        final ObjectId randomUserId = new ObjectId();
        final ErrorResponse errorResponse = given().port(port)
                .contentType(ContentType.JSON)
                .header("user_id", randomUserId)
                .body("{\"x\": 1, \"y\": -1}")
                .post("/minesweeper/" + randomGameId + "/click")
                .body().as(ErrorResponse.class);
        assertEquals("Y must be equal or greater than 0", errorResponse.getErrorMessage());
    }

    @Test
    public void tryToClickWithInvalidXYCoordinateAPITest() {
        final ObjectId randomGameId = new ObjectId();
        final ObjectId randomUserId = new ObjectId();
        final ErrorResponse errorResponse = given().port(port)
                .contentType(ContentType.JSON)
                .header("user_id", randomUserId)
                .body("{\"x\": -1, \"y\": -1}")
                .post("/minesweeper/" + randomGameId + "/click")
                .body().as(ErrorResponse.class);
        assertTrue(errorResponse.getErrorMessage().contains("X must be equal or greater than 0"));
        assertTrue(errorResponse.getErrorMessage().contains("Y must be equal or greater than 0"));
    }

    @Test
    public void clickNonExistentGameAPITest() {
        final UserResponse user = createUser();
        final ObjectId randomGameId = new ObjectId();
        final ErrorResponse errorResponse = given().port(port)
                .contentType(ContentType.JSON)
                .header("user_id", user.getId())
                .body("{\"x\": 1, \"y\": 1}")
                .post("/minesweeper/" + randomGameId + "/click")
                .body().as(ErrorResponse.class);
        assertEquals(String.format("Game %s not found for user %s", randomGameId, user.getId()),
                errorResponse.getErrorMessage());
    }

    @Test
    public void clickInvalidGameIdAPITest() {
        final UserResponse user = createUser();
        final ErrorResponse errorResponse = given().port(port)
                .contentType(ContentType.JSON)
                .header("user_id", user.getId())
                .body("{\"x\": 1, \"y\": 1}")
                .post("/minesweeper/invalidId/click")
                .body().as(ErrorResponse.class);
        assertEquals("Invalid game id format. Received: invalidId", errorResponse.getErrorMessage());
    }

    @Test
    public void clickDifferentUserAPITest() {
        final GameResponse gameResponseCreate = createGame();
        final ObjectId randomUserId = new ObjectId();
        final ErrorResponse errorResponse = given().port(port)
                .contentType(ContentType.JSON)
                .header("user_id", randomUserId.toString())
                .body("{\"x\": 1, \"y\": 1}")
                .post("/minesweeper/" + gameResponseCreate.getGameId() + "/click")
                .body().as(ErrorResponse.class);
        assertEquals(String.format("Game %s not found for user %s", gameResponseCreate.getGameId(), randomUserId),
                errorResponse.getErrorMessage());

        final ClickResponse clickResponse = given().port(port)
                .contentType(ContentType.JSON)
                .header("user_id", gameResponseCreate.getUserId())
                .body("{\"x\": 1, \"y\": 1}")
                .post("/minesweeper/" + gameResponseCreate.getGameId() + "/click")
                .body().as(ClickResponse.class);
        assertEquals(gameResponseCreate.getGameId(), clickResponse.getGameResponse().getGameId());
    }

    @Test
    public void flagAPITest() {
        final GameResponse gameResponseCreate = createGame();
        final ClickResponse clickResponse = given().port(port)
                .contentType(ContentType.JSON)
                .header("user_id", gameResponseCreate.getUserId())
                .body("{\"x\": 1, \"y\": 1}")
                .post("/minesweeper/" + gameResponseCreate.getGameId() + "/flag")
                .body().as(ClickResponse.class);
        assertEquals(gameResponseCreate.getGameId(), clickResponse.getGameResponse().getGameId());
        assertEquals(ClickResult.FLAGGED, clickResponse.getClickResult());
    }

    @Test
    public void tryToFlagWithInvalidXCoordinateAPITest() {
        final ObjectId randomGameId = new ObjectId();
        final ObjectId randomUserId = new ObjectId();
        final ErrorResponse errorResponse = given().port(port)
                .contentType(ContentType.JSON)
                .header("user_id", randomUserId)
                .body("{\"x\": -1, \"y\": 1}")
                .post("/minesweeper/" + randomGameId + "/flag")
                .body().as(ErrorResponse.class);
        assertEquals("X must be equal or greater than 0", errorResponse.getErrorMessage());
    }

    @Test
    public void tryToFlagWithInvalidYCoordinateAPITest() {
        final ObjectId randomGameId = new ObjectId();
        final ObjectId randomUserId = new ObjectId();
        final ErrorResponse errorResponse = given().port(port)
                .contentType(ContentType.JSON)
                .header("user_id", randomUserId)
                .body("{\"x\": 1, \"y\": -1}")
                .post("/minesweeper/" + randomGameId + "/flag")
                .body().as(ErrorResponse.class);
        assertEquals("Y must be equal or greater than 0", errorResponse.getErrorMessage());
    }

    @Test
    public void tryToFlagWithInvalidXYCoordinateAPITest() {
        final ObjectId randomGameId = new ObjectId();
        final ObjectId randomUserId = new ObjectId();
        final ErrorResponse errorResponse = given().port(port)
                .contentType(ContentType.JSON)
                .header("user_id", randomUserId)
                .body("{\"x\": -1, \"y\": -1}")
                .post("/minesweeper/" + randomGameId + "/flag")
                .body().as(ErrorResponse.class);
        assertTrue(errorResponse.getErrorMessage().contains("X must be equal or greater than 0"));
        assertTrue(errorResponse.getErrorMessage().contains("Y must be equal or greater than 0"));
    }

    @Test
    public void flagNonExistentGameAPITest() {
        final UserResponse user = createUser();
        final ObjectId randomGameId = new ObjectId();
        final ErrorResponse errorResponse = given().port(port)
                .contentType(ContentType.JSON)
                .header("user_id", user.getId())
                .body("{\"x\": 1, \"y\": 1}")
                .post("/minesweeper/" + randomGameId + "/flag")
                .body().as(ErrorResponse.class);
        assertEquals(String.format("Game %s not found for user %s", randomGameId, user.getId()),
                errorResponse.getErrorMessage());
    }

    @Test
    public void flagInvalidGameIdAPITest() {
        final UserResponse user = createUser();
        final ErrorResponse errorResponse = given().port(port)
                .contentType(ContentType.JSON)
                .header("user_id", user.getId())
                .body("{\"x\": 1, \"y\": 1}")
                .post("/minesweeper/invalidId/flag")
                .body().as(ErrorResponse.class);
        assertEquals("Invalid game id format. Received: invalidId", errorResponse.getErrorMessage());
    }

    @Test
    public void flagDifferentUserAPITest() {
        final GameResponse gameResponseCreate = createGame();
        final ObjectId randomUserId = new ObjectId();
        final ErrorResponse errorResponse = given().port(port)
                .contentType(ContentType.JSON)
                .header("user_id", randomUserId.toString())
                .body("{\"x\": 1, \"y\": 1}")
                .post("/minesweeper/" + gameResponseCreate.getGameId() + "/flag")
                .body().as(ErrorResponse.class);
        assertEquals(String.format("Game %s not found for user %s", gameResponseCreate.getGameId(), randomUserId),
                errorResponse.getErrorMessage());

        final ClickResponse clickResponse = given().port(port)
                .contentType(ContentType.JSON)
                .header("user_id", gameResponseCreate.getUserId())
                .body("{\"x\": 1, \"y\": 1}")
                .post("/minesweeper/" + gameResponseCreate.getGameId() + "/flag")
                .body().as(ClickResponse.class);
        assertEquals(gameResponseCreate.getGameId(), clickResponse.getGameResponse().getGameId());
    }

    private GameResponse createGame() {
        final UserResponse user = createUser();
        Map<String, Object> jsonAsMap = new HashMap<>();
        jsonAsMap.put("board_height", "2");
        jsonAsMap.put("board_width", "3");
        jsonAsMap.put("bombs_count", "1");
        return given().port(port)
                .contentType(ContentType.JSON)
                .header("user_id", user.getId())
                .body(jsonAsMap)
                .post("/minesweeper")
                .body().as(GameResponse.class);
    }

    private UserResponse createUser() {
        Map<String, Object> jsonAsMap = new HashMap<>();
        jsonAsMap.put("name", "test name");
        return given().port(port)
                .contentType(ContentType.JSON)
                .body(jsonAsMap)
                .post("/user")
                .body().as(UserResponse.class);
    }
}
