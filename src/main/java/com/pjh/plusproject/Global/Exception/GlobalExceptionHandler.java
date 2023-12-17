package com.pjh.plusproject.Global.Exception;

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
        Date now = new Date();
        return new ExceptionResponseDTO<>("NoSuchElementException", HttpStatusCode.BAD_REQUEST, LocalDateTime.now());
    }
}
