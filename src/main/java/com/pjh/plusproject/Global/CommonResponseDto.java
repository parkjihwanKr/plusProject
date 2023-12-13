package com.pjh.plusproject.Global;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class CommonResponseDto<T> {
    private String message;
    private int stateCode;
    private T data;

    public CommonResponseDto(String messageString, int stateCode){
        this.message = message;
        this.stateCode = stateCode;
        this.data = null;
    }
}
