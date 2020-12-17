package com.patriciomascialino.minesweeper.api.request;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.patriciomascialino.minesweeper.model.Coordinate;
import io.swagger.annotations.ApiParam;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Min;

@AllArgsConstructor
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
@NoArgsConstructor
public class ClickRequest {
    @ApiParam(value = "The clicked column number, zero based")
    @Min(value = 0, message = "X must be equal or greater than 0")
    private int x;
    @ApiParam(value = "The clicked row number, zero based")
    @Min(value = 0, message = "Y must be equal or greater than 0")
    private int y;

    public Coordinate toCoordinate() {
        return new Coordinate(this.x, this.y);
    }
}
