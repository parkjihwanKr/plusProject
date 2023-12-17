package com.pjh.plusproject.Global.Exception;

import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
public enum HttpStatusCode {
    // 200
    OK(HttpStatus.OK, "OK"),
    // 201
    CREATED(HttpStatus.CREATED, "Created"),
    // 400
    BAD_REQUEST(HttpStatus.BAD_REQUEST, "잘못된 요청입니다."),
    // 404
    POSTS_NOT_FOUND(HttpStatus.NOT_FOUND, "해당 게시물을 찾을 수 없습니다."),
    // 403
    NOT_AUTHORIZED_MEMBER(HttpStatus.UNAUTHORIZED, "해당 멤버는 권한이 없습니다."),
    // 405
    METHOD_NOT_ALLOWED(HttpStatus.METHOD_NOT_ALLOWED, "허용되지 않은 메서드입니다."),
    // 500
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "내부 서버 오류입니다.");

    private final HttpStatus httpStatus;
    private final String message;

    // Custom 생성자
    HttpStatusCode(HttpStatus httpStatus, String customMessage) {
        this.httpStatus = httpStatus;
        this.message = customMessage;
    }
}
