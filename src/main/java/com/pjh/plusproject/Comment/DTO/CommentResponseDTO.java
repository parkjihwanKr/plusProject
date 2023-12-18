package com.pjh.plusproject.Comment.DTO;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Builder
@Getter
@Setter
public class CommentResponseDTO {
    private long commentId;
    // 작성자
    private String writer;

    // 작성자의 댓글 내용
    private String content;

    private LocalDateTime createdAt;
}
