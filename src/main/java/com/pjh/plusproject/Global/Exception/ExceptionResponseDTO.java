package com.pjh.plusproject.Global.Exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ExceptionResponseDTO<T> {
    private String message;
    // 열거형으로 저장 할 예정
    private T httpStatusCode;
    private LocalDateTime timeStamp;

    // Exception에 대한 DTO 처리를 custom하여 처리
}
