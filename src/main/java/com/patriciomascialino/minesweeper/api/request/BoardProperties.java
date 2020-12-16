package com.patriciomascialino.minesweeper.api.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@AllArgsConstructor
@Data
@NoArgsConstructor
public class BoardProperties {
    @JsonProperty("board_height")
    private int boardHeight;
    @JsonProperty("board_width")
    private int boardWidth;
    @JsonProperty("bombs_count")
    private int bombsCount;
}
