package com.pjh.plusproject.Board.Entity;

import com.pjh.plusproject.Board.DTO.BoardResponseDTO;
import com.pjh.plusproject.Global.Common.BaseEntity;
import com.pjh.plusproject.Member.Entity.Member;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Board extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    private String description;

    private String imageUrl;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "member_id")
    private Member member;
    // 지연 로딩 전략 -> 즉시 로딩 변경

    public BoardResponseDTO showBoard(Board board){
        return BoardResponseDTO.builder()
                .memberId(board.getMember().getId())
                .boardId(board.getId())
                .createAt(board.getCreatedAt())
                .description(board.getDescription())
                .title(board.getTitle())
                .writer(board.getMember().getUsername())
                .build();
    }
}
