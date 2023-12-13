package com.pjh.plusproject.Global.Common;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class CommonResponseDto<T> {
    private String message;
    private int statusCode;
    private T data;

    public CommonResponseDto(String message, int statusCode){
        this.message = message;
        this.statusCode = statusCode;
    }
}
