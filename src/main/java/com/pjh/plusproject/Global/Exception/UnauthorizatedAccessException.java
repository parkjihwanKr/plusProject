package com.pjh.plusproject.Global.Exception;

public class UnauthorizatedAccessException extends RuntimeException{
    public UnauthorizatedAccessException(String message){
        super(message);
    }
}
