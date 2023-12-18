package com.pjh.plusproject.Comment.Service;

import com.pjh.plusproject.Board.Entity.Board;
import com.pjh.plusproject.Board.Repository.BoardRepository;
import com.pjh.plusproject.Comment.DTO.CommentRequestDTO;
import com.pjh.plusproject.Comment.DTO.CommentResponseDTO;
import com.pjh.plusproject.Comment.Entity.Comment;
import com.pjh.plusproject.Comment.Repository.CommentRepository;
import com.pjh.plusproject.Global.Common.CommonResponseDto;
import com.pjh.plusproject.Global.Exception.HttpStatusCode;
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

    // 해당 조회 부분 안 만들었네?
    public CommonResponseDto<?> showBoardAllComment(long boardId) {
        Board board = boardRepository.findById(boardId).orElseThrow();
        return null;
    }
    public CommonResponseDto<?> createComment(long boardId, CommentRequestDTO commentRequestDTO) {
        // 한 게시글에 여러 개의 댓글이 달릴 수 있음

        // 인증된 사용자의 memberName을 가져옴
        String memberName = SecurityContextHolder.getContext().getAuthentication().getName();
        // 만약에 인증되지 않은 사용자가 들어가면? -> 이는 WebSecurityConfig.java에서 막을꺼기 때문에
        // Service단까지 들어올 수 없음. 그러므로 memberName은 null이거나 공백의 문자열이 들어갈 수 없음
        log.info("memberName : "+memberName);
        Member member = memberRepository.findByUsername(memberName).orElseThrow();
        // 에러가 났을 때는 예외처리를 하는게 좋다.
        // 명시적으로 exception를 찍어주는게 좋다.
        Board board = boardRepository.findById(boardId).orElseThrow();

        Comment comment = Comment.builder()
                .content(commentRequestDTO.getContent())
                .member(member)
                .board(board)
                .build();

        commentRepository.save(comment);

        CommentResponseDTO responseDTO = comment.showResponseDTO(comment);
        return new CommonResponseDto<>("댓글 작성 성공", HttpStatusCode.OK, responseDTO);
    }

    public CommonResponseDto<?> updateComment(long commentId, CommentRequestDTO requestDTO) {
        Comment comment = commentRepository.findById(commentId).orElseThrow();
        boardRepository.findById(comment.getBoard().getId()).orElseThrow();
        memberRepository.findById(comment.getMember().getId()).orElseThrow();
        comment.updateComment(requestDTO.getContent());
        commentRepository.save(comment);
        CommentResponseDTO responseDTO = comment.showResponseDTO(comment);

        return new CommonResponseDto<>("댓글 수정 성공", HttpStatusCode.OK, responseDTO);
    }

    public CommonResponseDto<?> deleteComment(long commentId) {
        Comment comment = commentRepository.findById(commentId).orElseThrow();
        commentRepository.deleteById(commentId);
        return new CommonResponseDto<>("댓글 삭제 성공", HttpStatusCode.OK, null);
    }

    /*
    [ ]  챌린지 과제) 전체 조회가 아닌 페이징 조회를 할 수 있도록 해보기
    [ ]  (챌린지 과제) 페이징 + 커스텀 정렬 기능 구현하기 -> 사용자가 입력한 key와
    정렬 기준을 동적으로 입력 받아, 해당 기준에 맞게 데이터를 제공.
    (예. 작성자명 오름차순 정렬 and 작성 날짜 오름차순 정렬된 결과를 상위 5개만 출력)
    */
}
