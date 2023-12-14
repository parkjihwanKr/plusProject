package com.pjh.plusproject.Board.Service;

import com.pjh.plusproject.Board.DTO.BoardRequestDTO;
import com.pjh.plusproject.Board.DTO.BoardResponseDTO;
import com.pjh.plusproject.Board.Entity.Board;
import com.pjh.plusproject.Board.Repository.BoardRepository;
import com.pjh.plusproject.Global.Common.CommonResponseDto;
import com.pjh.plusproject.Global.Security.MemberDetailsImpl;
import com.pjh.plusproject.Member.Entity.Member;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class BoardService {
    private final BoardRepository boardRepository;

    @Transactional(readOnly = true)
    public CommonResponseDto<?> getAllBoardList(){
        List<Board> boardList = boardRepository.findAll();
        return new CommonResponseDto<>("모든 게시글 조회", 200, boardList);
    }

    @Transactional
    public CommonResponseDto<?> createBoard(BoardRequestDTO requestDTO, MemberDetailsImpl memberDetails) {

        Board boardEntity = Board.builder()
                .title(requestDTO.getTitle())
                .description(requestDTO.getDescription())
                .member(memberDetails.getMember())
                .build();

        boardRepository.save(boardEntity);
        BoardResponseDTO responseDTO = boardEntity.showBoard(boardEntity);
        return new CommonResponseDto<>("게시글 작성 성공", 200, responseDTO);
    }
}
