package com.patriciomascialino.minesweeper.api.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiParam;
import lombok.*;

import javax.validation.constraints.Min;

@AllArgsConstructor
@Data
@NoArgsConstructor
public class BoardProperties {
    @ApiParam(value = "The amount of rows that the board should have")
    @JsonProperty("board_height")
    @Min(value = 1, message = "The board height (rows) must be equal or greater than 1")
    private int boardHeight;

    @ApiParam(value = "The amount of rows that the board should have")
    @JsonProperty("board_width")
    @Min(value = 1, message = "The board width (columns) must be equal or greater than 1")
    private int boardWidth;

    @ApiParam(value = "The amount of columns that the board should have")
    @Min(value = 1, message = "There should be at least 1 bomb")
    @JsonProperty("bombs_count")
    private int bombsCount;
}
