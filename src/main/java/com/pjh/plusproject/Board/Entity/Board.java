package com.pjh.plusproject.Board.Entity;

import com.pjh.plusproject.Board.DTO.BoardRequestDTO;
import com.pjh.plusproject.Board.DTO.BoardResponseDTO;
import com.pjh.plusproject.Comment.Entity.Comment;
import com.pjh.plusproject.Global.Common.BaseEntity;
import com.pjh.plusproject.Member.Entity.Member;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

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

    // 지연 로딩 전략 -> 즉시 로딩 변경
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "member_id")
    private Member member;

    // 모든 변경 작업에 cascade 실행
    // 댓글이 게시글과 연관이 되어있지 않으면 삭제
    // on delete cascade
    @OneToMany(mappedBy = "board", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Comment> comments = new LinkedHashSet<>();

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

    public void update(BoardRequestDTO boardRequestDTO) {
        this.title = boardRequestDTO.getTitle();
        this.description = boardRequestDTO.getDescription();
    }

    public BoardResponseDTO showUpdateBoard(Board board){
        return BoardResponseDTO.builder()
                .boardId(board.getId())
                .memberId(board.getMember().getId())
                .writer(board.getMember().getUsername())
                .title(board.getTitle())
                .description(board.getDescription())
                .createAt(board.getCreatedAt())
                .build();
    }
}
