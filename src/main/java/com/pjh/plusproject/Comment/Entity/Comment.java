package com.pjh.plusproject.Comment.Entity;

import com.pjh.plusproject.Board.Entity.Board;
import com.pjh.plusproject.Comment.DTO.CommentRequestDTO;
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
public class Comment extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private String content;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "member_id")
    private Member member; // 댓글 작성자

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "board_id")
    private Board board; // 댓글이 달린 게시물
    // 지연 로딩에서 즉시 로딩 변경

}
