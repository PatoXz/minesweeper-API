package com.patriciomascialino.minesweeper.api.exceptionhandler;

import com.patriciomascialino.minesweeper.api.MineSweeperController;
import com.patriciomascialino.minesweeper.exception.GameNotFoundException;
import com.patriciomascialino.minesweeper.exception.InvalidGameIdException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.stream.Collectors;

@ControllerAdvice(assignableTypes = MineSweeperController.class)
@RequestMapping(produces = MediaType.APPLICATION_JSON_VALUE)
@Slf4j
public class MineSweeperExceptionHandler {
    @ExceptionHandler(GameNotFoundException.class)
    public ResponseEntity<String> handleFirstShotNotFound(GameNotFoundException ex) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(InvalidGameIdException.class)
    public ResponseEntity<String> handleFirstShotNotFound(InvalidGameIdException ex) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<?> methodArgumentNotValidException(MethodArgumentNotValidException ex) {
        BindingResult result = ex.getBindingResult();
        String errorMessage = result.getFieldErrors().stream()
                .map(DefaultMessageSourceResolvable::getDefaultMessage)
                .collect(Collectors.joining("\n"));
        return ResponseEntity.badRequest().body(errorMessage);
    }
}
