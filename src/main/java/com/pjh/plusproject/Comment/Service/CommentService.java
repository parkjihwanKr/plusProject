package com.pjh.plusproject.Comment.Service;

import com.pjh.plusproject.Board.Entity.Board;
import com.pjh.plusproject.Board.Repository.BoardRepository;
import com.pjh.plusproject.Comment.DTO.CommentRequestDTO;
import com.pjh.plusproject.Comment.DTO.CommentResponseDTO;
import com.pjh.plusproject.Comment.Entity.Comment;
import com.pjh.plusproject.Comment.Repository.CommentRepository;
import com.pjh.plusproject.Global.Common.CommonResponseDto;
import com.pjh.plusproject.Member.Entity.Member;
import com.pjh.plusproject.Member.Repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;
    private final BoardRepository boardRepository;
    private final MemberRepository memberRepository;
    public CommonResponseDto<?> createComment(long boardId, CommentRequestDTO commentRequestDTO) {
        // 한 게시글에 여러 개의 댓글이 달릴 수 있음

        // 인증된 사용자의 memberName을 가져옴
        String memberName = SecurityContextHolder.getContext().getAuthentication().getName();
        // 만약에 인증되지 않은 사용자가 들어가면? -> 이는 WebSecurityConfig.java에서 막을꺼기 때문에
        // Service단까지 들어올 수 없음. 그러므로 memberName은 null이거나 공백의 문자열이 들어갈 수 없음
        log.info("memberName : "+memberName);
        Member member = memberRepository.findByUsername(memberName).orElse(null);
        if(member == null){
            return new CommonResponseDto<>("해당하는 멤버네임이 없습니다.", 400, null);
        }
        Board board = boardRepository.findById(boardId).orElse(null);
        if(board == null){
            return new CommonResponseDto<>("해당하는 게시글이 존재하지 않습니다.", 400, null);
        }

        Comment comment = Comment.builder()
                .content(commentRequestDTO.getContent())
                .member(member)
                .board(board)
                .build();

        commentRepository.save(comment);

        CommentResponseDTO responseDTO = CommentResponseDTO.builder()
                .createdAt(comment.getCreatedAt())
                .content(comment.getContent())
                .writer(comment.getMember().getUsername())
                .build();

        return new CommonResponseDto<>("댓글 작성 성공", 201, responseDTO);
    }
}
