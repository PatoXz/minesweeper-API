package com.patriciomascialino.minesweeper.api.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.patriciomascialino.minesweeper.model.User;
import io.swagger.annotations.ApiParam;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.ZonedDateTime;

@NoArgsConstructor
@Getter
@JsonIgnoreProperties(ignoreUnknown = true)
public class UserResponse {
    @ApiParam(value = "Id of the user")
    private String id;
    @ApiParam(value = "Name of the user")
    private String name;
    @ApiParam(value = "User since")
    @JsonProperty("created_at")
    private ZonedDateTime createdAt;

    public static UserResponse of(User user) {
        UserResponse userResponse = new UserResponse();
        userResponse.id = user.getId().toString();
        userResponse.name = user.getName();
        userResponse.createdAt = user.getCreatedAt();
        return userResponse;
    }
}
