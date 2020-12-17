package com.patriciomascialino.minesweeper.api;

import com.patriciomascialino.minesweeper.api.request.UserRequest;
import com.patriciomascialino.minesweeper.api.response.UserResponse;
import com.patriciomascialino.minesweeper.model.User;
import com.patriciomascialino.minesweeper.service.UserService;
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
@RequestMapping("/user")
@Slf4j
public class UserController {
    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @ApiOperation(value = "Creates a new user", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiResponses({@ApiResponse(code = 200, message = "User created", response = User.class)})
    @PostMapping
    public ResponseEntity<UserResponse> newUser(
            @RequestBody @Valid final UserRequest userRequest) {
        log.info("New user request {}", userRequest.getName());
        final User user = userService.newUser(userRequest.getName());
        return ResponseEntity.ok(UserResponse.of(user));
    }

    @ApiOperation(value = "Get an existing user by its id", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiResponses({
        @ApiResponse(code = 200, message = "User found", response = User.class),
        @ApiResponse(code = 404, message = "User not found", response = String.class)
    })
    @GetMapping("/{userId}")
    public ResponseEntity<UserResponse> getUser(
            @PathVariable("userId") @ApiParam(value = "The user to look for") @Valid @NotBlank
            final String userId) {
        log.info("Get user request. userId: {}", userId);
        final User user = userService.getUser(userId);
        return ResponseEntity.ok(UserResponse.of(user));
    }
}
