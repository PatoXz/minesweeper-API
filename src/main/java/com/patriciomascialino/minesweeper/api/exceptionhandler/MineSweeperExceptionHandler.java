package com.patriciomascialino.minesweeper.api.exceptionhandler;

import com.patriciomascialino.minesweeper.api.MineSweeperController;
import com.patriciomascialino.minesweeper.api.response.ErrorResponse;
import com.patriciomascialino.minesweeper.exception.*;
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
    public ResponseEntity<ErrorResponse> handleGameNotFoundExceptionFound(GameNotFoundException ex) {
        return new ResponseEntity<>(new ErrorResponse(ex), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleUserNotFoundException(UserNotFoundException ex) {
        return new ResponseEntity<>(new ErrorResponse(ex), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(InvalidGameIdException.class)
    public ResponseEntity<ErrorResponse> handleInvalidGameIdException(InvalidGameIdException ex) {
        return new ResponseEntity<>(new ErrorResponse(ex), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(InvalidUserIdException.class)
    public ResponseEntity<ErrorResponse> handleInvalidUserIdException(InvalidUserIdException ex) {
        return new ResponseEntity<>(new ErrorResponse(ex), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(NotEnoughFreeCellsOnBoardException.class)
    public ResponseEntity<ErrorResponse> handleNotEnoughFreeCellsOnBoardException(NotEnoughFreeCellsOnBoardException ex) {
        return new ResponseEntity<>(new ErrorResponse(ex), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> methodArgumentNotValidException(MethodArgumentNotValidException ex) {
        BindingResult result = ex.getBindingResult();
        String errorMessage = result.getFieldErrors().stream()
                .map(DefaultMessageSourceResolvable::getDefaultMessage)
                .collect(Collectors.joining("\n"));
        return ResponseEntity.badRequest().body(new ErrorResponse(errorMessage));
    }
}
