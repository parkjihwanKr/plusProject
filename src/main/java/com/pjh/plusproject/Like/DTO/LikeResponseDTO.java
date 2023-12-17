package com.pjh.plusproject.Like.DTO;

import com.pjh.plusproject.Board.Entity.Board;
import com.pjh.plusproject.Member.Entity.Member;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
@AllArgsConstructor
public class LikeResponseDTO {

    private String toMemberName;
    private String fromMemberName;
    private String boardTitle;
    private String boardDescription;

    // 유저 좋아요
    // from Member name을 받는 거는... 로그인 사용자고
    // to Member는 정보를 어디서 가져와야할까? memberId로 가져오고
    public LikeResponseDTO(Member member, String fromMemberName){
        this.toMemberName = member.getUsername();
        this.fromMemberName = fromMemberName;
    }

    // 게시글 좋아요
    public LikeResponseDTO(Board board, String fromMemberName){
        this.fromMemberName = fromMemberName;
        this.boardTitle = board.getTitle();
        this.boardDescription = board.getDescription();
    }
}
