package com.pjh.plusproject.Board.DTO;

import com.pjh.plusproject.Board.Entity.Board;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
public class BoardResponseDTO {
    private Long memberId;
    private Long boardId;
    private String writer;
    private String title;
    private String description;
    private LocalDateTime createAt;
}
