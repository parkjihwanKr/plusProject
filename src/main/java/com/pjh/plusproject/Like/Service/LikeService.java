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
import org.springframework.transaction.annotation.Transactional;

import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
public class LikeService {
    private final LikeRepository likeRepository;
    private final BoardRepository boardRepository;
    private final MemberRepository memberRepository;

    public CommonResponseDto<?> likeBoard(long boardId) {
        // NoSuchElementException
        Board board = boardRepository.findById(boardId).orElseThrow(
                ()-> new NoSuchElementException("해당하는 게시글은 존재하지 않습니다.")
        );

        // 자기 자신이? 좋아요를 누르면?
        // 로그인 멤버의 네임를 가져옴
        String loginMemberName = getLoginMemberName();
        if(loginMemberName.equals(board.getMember().getUsername())){
            // 로그인한 사용자와 게시글의 사용자를 좋아요 할 수 없게 만드는 로직
            throw new IllegalArgumentException("자신이 작성한 게시글에 '좋아요'를 누를 수 없습니다.");
        }

        // 똑같은 게시글에 계속 '좋아요'를 누르면?
        // deleteById로 같이 작동 시켰어야..?
        // front에서 작동하는게 더 좋을듯? 싶음, refactoring할꺼라 test code 작성 필요 없을듯?
        // likeRepository.existsByBoardIdAndToMemberId(boardId, board.getMember().getId()).orElseThrow();
        boolean hasLiked = likeRepository.existsByBoardIdAndFromMemberId(boardId, board.getMember().getId());
        if (hasLiked) {
            throw new IllegalArgumentException("해당 멤버는 '좋아요'를 누른 게시글입니다.");
        }

        Like like = Like.builder()
                .board(board)
                .fromMember(board.getMember())
                .build();
        likeRepository.save(like);

        LikeResponseDTO responseDTO = LikeResponseDTO.builder()
                .toMemberName(board.getMember().getUsername())
                .fromMemberName(loginMemberName)
                .boardTitle(board.getTitle())
                .boardDescription(board.getDescription())
                .build();

        return new CommonResponseDto<>("해당 게시글 좋아요 성공", HttpStatusCode.OK, responseDTO);
    }

    @Transactional
    public CommonResponseDto<?> unlikeBoard(long boardId) {
        // boardId 찾기
        Board board = boardRepository.findById(boardId).orElseThrow(
                ()-> new NoSuchElementException("해당하는 게시글은 존재하지 않습니다.")
        );
        // likeRepository에 해당하는 boardId 존재하는지 확인
        // 있으면 삭제 비지니스 로직 수행, 없으면 삭제 비지니스가 아닌 예외처리로
        boolean hasLiked = likeRepository.existsByBoardIdAndFromMemberId(boardId, board.getMember().getId());
        if(!hasLiked){
            throw new IllegalArgumentException("해당 멤버는 해당 게시글에 대한 '좋아요'를 누르지 않았습니다.");
        }
        likeRepository.deleteByBoardId(boardId);
        return new CommonResponseDto<>("해당 게시글 좋아요 취소 성공", HttpStatusCode.OK, null);
    }

    public CommonResponseDto<?> likeMember(long toMemberId){
        // '좋아요'를 받는 멤버 존재하지는지?
        Member toMember = memberRepository.findById(toMemberId).orElseThrow(
                ()-> new NoSuchElementException("해당 좋아요를 하려는 멤버는 존재하지 않습니다.")
        );
        String fromMemberName = getLoginMemberName();
        Member fromMember = memberRepository.findByUsername(fromMemberName).orElseThrow(
                ()-> new NoSuchElementException("해당 멤버는 존재하지 않습니다.")
        );

        boolean hasLiked = likeRepository.existsByToMemberIdAndFromMemberId(fromMember.getId(), toMember.getId());
        if (hasLiked) {
            throw new IllegalArgumentException("해당 멤버는 '좋아요'를 누른 멤버입니다.");
        }

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

        return new CommonResponseDto<>("해당 멤버 좋아요 성공", HttpStatusCode.OK, likeResponseDTO);
    }

    @Transactional
    public CommonResponseDto<?> unlikeMember(long memberId){
        Member toMember = memberRepository.findById(memberId).orElseThrow();
        Member fromMember = memberRepository.findByUsername(getLoginMemberName()).orElseThrow();
        boolean hasLiked = likeRepository.existsByToMemberIdAndFromMemberId(fromMember.getId(), toMember.getId());
        if (!hasLiked) {
            throw new IllegalArgumentException("해당 멤버는 '좋아요'를 누르지 않은 멤버입니다.");
        }

        likeRepository.deleteByToMemberId(memberId);
        return new CommonResponseDto<>("해당 멤버 좋아요 취소 성공", HttpStatusCode.OK, null);
    }


    private String getLoginMemberName(){
        return SecurityContextHolder.getContext().getAuthentication().getName();
    }

}
