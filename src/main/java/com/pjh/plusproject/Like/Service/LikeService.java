package com.pjh.plusproject.Like.Service;

import com.pjh.plusproject.Board.Entity.Board;
import com.pjh.plusproject.Board.Repository.BoardRepository;
import com.pjh.plusproject.Global.Common.CommonResponseDto;
import com.pjh.plusproject.Global.Exception.HttpStatusCode;
import com.pjh.plusproject.Like.DTO.LikeResponseDTO;
import com.pjh.plusproject.Like.Entity.Like;
import com.pjh.plusproject.Like.Repository.LikeRepository;
import com.pjh.plusproject.Member.Entity.Member;
import com.pjh.plusproject.Member.Repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.HashSet;

@Service
@RequiredArgsConstructor
public class LikeService {
    private final LikeRepository likeRepository;
    private final BoardRepository boardRepository;
    private final MemberRepository memberRepository;

    public CommonResponseDto<?> likeBoard(long boardId) {
        // NoSuchElementException
        Board board = boardRepository.findById(boardId).orElseThrow();
        Like like = Like.builder()
                .board(board)
                .fromMember(board.getMember())
                .build();
        HashMap<String, Integer> set = new HashMap<>();

        likeRepository.save(like);

        // 로그인 멤버의 네임를 가져옴
        String loginMemberName = getLoginMemberName();

        /*
        private String toMemberName;
        private String fromMemberName;
        private String boardTitle;
        private String boardDescription;
        */
        LikeResponseDTO responseDTO = LikeResponseDTO.builder()
                .toMemberName(board.getMember().getUsername())
                .fromMemberName(loginMemberName)
                .boardTitle(board.getTitle())
                .boardDescription(board.getDescription())
                .build();

        return new CommonResponseDto<>("게시글 좋아요 성공", HttpStatusCode.OK, responseDTO);
    }

    public CommonResponseDto<?> unlikeBoard(long boardId) {
        boardRepository.findById(boardId).orElseThrow();
        likeRepository.deleteByBoardId(boardId);
        return new CommonResponseDto<>("게시글 좋아요 취소 성공", HttpStatusCode.OK, null);
    }

    public CommonResponseDto<?> likeMember(long memberId){
        // '좋아요'를 받는 멤버 존재하지는지?
        Member toMember = memberRepository.findById(memberId).orElseThrow();
        String fromMemberName = getLoginMemberName();
        Member fromMember = memberRepository.findByUsername(fromMemberName).orElseThrow();
        // like entity 저장
        Like like = Like.builder()
                .fromMember(fromMember)
                .toMember(toMember)
                .build();
        likeRepository.save(like);

        LikeResponseDTO likeResponseDTO = LikeResponseDTO.builder()
                .fromMemberName(fromMemberName)
                .toMemberName(toMember.getUsername())
                .build();

        return new CommonResponseDto<>("멤버 좋아요 성공", HttpStatusCode.OK, likeResponseDTO);
    }

    public CommonResponseDto<?> unlikeMember(long memberId){
        memberRepository.findById(memberId).orElseThrow();
        likeRepository.deleteByMemberId(memberId);
        return new CommonResponseDto<>("멤버 좋아요 취소 성공", HttpStatusCode.OK, null);
    }

    private String getLoginMemberName(){
        return SecurityContextHolder.getContext().getAuthentication().getName();
    }

}
