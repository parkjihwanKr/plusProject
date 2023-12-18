package com.pjh.plusproject.Global.Exception;

import io.jsonwebtoken.io.IOException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.NoSuchElementException;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(NoSuchElementException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    // 400 에러 발생 시
    public ExceptionResponseDTO<?> noSuchElementException(NoSuchElementException e){
        return new ExceptionResponseDTO<>(e.getMessage(), HttpStatusCode.BAD_REQUEST, LocalDateTime.now());
    }

    @ExceptionHandler(IllegalArgumentException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    // 400 에러 발생 시
    public ExceptionResponseDTO<?> illegalArgumentException(IllegalArgumentException e) {
        return new ExceptionResponseDTO<>(e.getMessage(), HttpStatusCode.BAD_REQUEST, LocalDateTime.now());
    }

    @ExceptionHandler(IOException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ExceptionResponseDTO<?> iOException(IOException e) {
        return new ExceptionResponseDTO<>(e.getMessage(), HttpStatusCode.INTERNAL_SERVER_ERROR, LocalDateTime.now());
    }

}