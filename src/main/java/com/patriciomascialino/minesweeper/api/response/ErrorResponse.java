package com.patriciomascialino.minesweeper.api.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@NoArgsConstructor
@Setter
public class ErrorResponse {
    @JsonProperty("error_message")
    private String errorMessage;

    public ErrorResponse(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public ErrorResponse(Exception ex) {
        this.errorMessage = ex.getMessage();
    }
}
