package com.pjh.plusproject.Global.Common;

import com.pjh.plusproject.Global.Exception.HttpStatusCode;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Date;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class CommonResponseDto<T>{
    private String message;
    private HttpStatusCode status;
    private T data;

    // 열거형 데이터 넣고 싶은데?
    public CommonResponseDto(String message, HttpStatusCode status){
        this.message = message;
        this.status = status;
    }

    // 오류 상황에서 처리하려고
}
