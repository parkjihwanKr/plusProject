package com.pjh.plusproject.Comment.Service;

import com.pjh.plusproject.Board.Entity.Board;
import com.pjh.plusproject.Board.Repository.BoardRepository;
import com.pjh.plusproject.Comment.DTO.CommentRequestDTO;
import com.pjh.plusproject.Comment.DTO.CommentResponseDTO;
import com.pjh.plusproject.Comment.Entity.Comment;
import com.pjh.plusproject.Comment.Repository.CommentRepository;
import com.pjh.plusproject.Global.DTO.CommonResponseDTO;
import com.pjh.plusproject.Global.Exception.HttpStatusCode;
import com.pjh.plusproject.Global.Exception.UnauthorizatedAccessException;
import com.pjh.plusproject.Member.Entity.Member;
import com.pjh.plusproject.Member.Repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

@Slf4j
@Service
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;
    private final BoardRepository boardRepository;
    private final MemberRepository memberRepository;

    public CommonResponseDTO<?> showBoardAllComment(long boardId) {
        boardRepository.findById(boardId).orElseThrow(
                ()-> new NoSuchElementException("해당 게시글을 찾을 수 없습니다.")
        );
        List<Comment> commentList = commentRepository.findByBoardId(boardId);
        List<CommentResponseDTO> responseDTOList = new ArrayList<>();
        for(int i = 0; i<commentList.size(); i++){
            responseDTOList.add(
                    CommentResponseDTO.builder()
                            .commentId(commentList.get(i).getId())
                            .createdAt(commentList.get(i).getCreatedAt())
                            .content(commentList.get(i).getContent())
                            .writer(commentList.get(i).getMember().getUsername())
                            .build()
            );
        }
        return new CommonResponseDTO<>("해당 게시글 모든 댓글 조회", HttpStatusCode.OK, responseDTOList);
    }
    public CommonResponseDTO<?> createComment(long boardId, CommentRequestDTO commentRequestDTO) {
        // 한 게시글에 여러 개의 댓글이 달릴 수 있음

        // 인증된 사용자의 memberName을 가져옴
        String memberName = loginMemberName();
        // 만약에 인증되지 않은 사용자가 들어가면? -> 이는 WebSecurityConfig.java에서 막을꺼기 때문에
        // Service단까지 들어올 수 없음. 그러므로 memberName은 null이거나 공백의 문자열이 들어갈 수 없음
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
        return new CommonResponseDTO<>("해당 게시글 댓글 작성 성공", HttpStatusCode.OK, responseDTO);
    }

    public CommonResponseDTO<?> updateComment(long commentId, CommentRequestDTO requestDTO) {
        // CommentRepository에 해당 id 존재하는지?
        Comment comment = commentRepository.findById(commentId).orElseThrow();
        // BoardRepository에 해당 게시글 존재하는지?
        boardRepository.findById(comment.getBoard().getId()).orElseThrow();
        // MemberRepository에 해당 멤버가 존재하는지?
        Member member = memberRepository.findById(comment.getMember().getId()).orElseThrow();
        if(!member.getUsername().equals(loginMemberName())){
            throw new UnauthorizatedAccessException("해당 멤버는 수정 권한이 없습니다.");
        }
        comment.updateComment(requestDTO.getContent());
        commentRepository.save(comment);
        CommentResponseDTO responseDTO = comment.showResponseDTO(comment);

        return new CommonResponseDTO<>("해당 게시글 댓글 수정 성공", HttpStatusCode.OK, responseDTO);
    }

    public CommonResponseDTO<?> deleteComment(long commentId) {
        Comment comment = commentRepository.findById(commentId).orElseThrow(
                ()-> new NoSuchElementException("해당 댓글은 존재하지 않습니다.")
        );
        if(!comment.getMember().getUsername().equals(loginMemberName())){
            throw new UnauthorizatedAccessException("해당 멤버는 삭제할 권한이 없습니다.");
        }
        commentRepository.deleteById(commentId);
        return new CommonResponseDTO<>("해당 게시글 댓글 삭제 성공", HttpStatusCode.OK, null);
    }
    private String loginMemberName(){
        return SecurityContextHolder.getContext().getAuthentication().getName();
    }
    /*
    [ ]  챌린지 과제) 전체 조회가 아닌 페이징 조회를 할 수 있도록 해보기
    [ ]  (챌린지 과제) 페이징 + 커스텀 정렬 기능 구현하기 -> 사용자가 입력한 key와
    정렬 기준을 동적으로 입력 받아, 해당 기준에 맞게 데이터를 제공.
    (예. 작성자명 오름차순 정렬 and 작성 날짜 오름차순 정렬된 결과를 상위 5개만 출력)
    */
}
