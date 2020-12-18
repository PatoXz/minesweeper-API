package com.patriciomascialino.minesweeper.api;

import com.patriciomascialino.minesweeper.api.response.ErrorResponse;
import com.patriciomascialino.minesweeper.api.response.UserResponse;
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
public class UserControllerTest {
    @LocalServerPort
    private int port;

    @Test
    public void createUserAPITest() {
        final UserResponse userResponse = createUser();
        assertEquals("test name", userResponse.getName());
        assertTrue(ObjectId.isValid(userResponse.getId()));
        assertNotNull(userResponse.getCreatedAt());
    }

    @Test
    public void tryToCreateUserWithEmptyNameAPITest() {
        Map<String, Object> jsonAsMap = new HashMap<>();
        jsonAsMap.put("name", "");
        final ErrorResponse errorResponse = given().port(port)
                .contentType(ContentType.JSON)
                .body(jsonAsMap)
                .post("/user")
                .body().as(ErrorResponse.class);
        assertEquals("The name of the user shouldn't be empty", errorResponse.getErrorMessage());
    }

    @Test
    public void getUserAPITest() {
        final UserResponse userResponseCreate = createUser();
        final UserResponse userResponseGet = given().port(port)
                .contentType(ContentType.JSON)
                .get("/user/" + userResponseCreate.getId())
                .body().as(UserResponse.class);
        assertEquals(userResponseCreate.getName(), userResponseGet.getName());
        assertEquals(userResponseCreate.getId(), userResponseGet.getId());
        assertEquals(userResponseCreate.getCreatedAt().toString().substring(0, 23),
                userResponseGet.getCreatedAt().toString().substring(0, 23));
    }

    @Test
    public void getNonExistentUserAPITest() {
        final ObjectId userId = new ObjectId();
        final ErrorResponse errorResponse = given().port(port)
                .contentType(ContentType.JSON)
                .get("/user/" + userId)
                .body().as(ErrorResponse.class);
        assertEquals("User not found. ID " + userId, errorResponse.getErrorMessage());
    }

    @Test
    public void invalidUserIdAPITest() {
        final ErrorResponse errorResponse = given().port(port)
                .contentType(ContentType.JSON)
                .get("/user/invalidId")
                .body().as(ErrorResponse.class);
        assertEquals("Invalid user id format. Received: invalidId", errorResponse.getErrorMessage());
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
