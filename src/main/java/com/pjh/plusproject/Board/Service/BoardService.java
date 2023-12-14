package com.pjh.plusproject.Board.Service;

import com.pjh.plusproject.Board.DTO.BoardRequestDTO;
import com.pjh.plusproject.Board.DTO.BoardResponseDTO;
import com.pjh.plusproject.Board.Entity.Board;
import com.pjh.plusproject.Board.Repository.BoardRepository;
import com.pjh.plusproject.Global.Common.CommonResponseDto;
import com.pjh.plusproject.Global.Security.MemberDetailsImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class BoardService {
    private final BoardRepository boardRepository;

    // 페이징 없이 만든 전체 board List 조회
    /*
    @Transactional(readOnly = true)
    public CommonResponseDto<?> getAllBoardList(Pageable pageable){
        // 여기에 담긴 boardPage를 모두 조회, 조건 없음

        List<Board> boardList = boardRepository.findAll();
        List<BoardResponseDTO> responseList = new ArrayList<>();

        for(int i = 0; i<boardList.size(); i++){
            Board board = boardList.get(i);
            responseList.add(
                    BoardResponseDTO.builder()
                            .memberId(board.getMember().getId())
                            .boardId(board.getId())
                            .title(board.getTitle())
                            .description(board.getDescription())
                            .createAt(board.getCreatedAt())
                            .writer(board.getMember().getUsername()).build()
            );
        }
        return new CommonResponseDto<>("모든 게시글 조회", 200, responseList);
    }*/

    // Page<BoardResponseDto> reponseList 형태로 repository 접근은 좋지 않음
    // 이유 : 역할의 분리 측면에서 좋지 않음
    // Presentation layer와 Service layer의 역할 분리, 변경의 유연성
    // Presentation layer는 데이터 전송에 역할을 맞춰야함
    // Service layer은 비지니스 로직의 역할에 충실해야함
    // 그러므로 Persistentce layer(repository)에 Dto형태가 들어가는 것은 말이 안됨
    // 만약 dto의 변경이 일어나면 dto, controller, service까지만 고치면 됨
    // 만약 DTO가 Persistence layer까지 간다면 코드 변경에 repository의 변경까지 이어짐
    @Transactional(readOnly = true)
    public CommonResponseDto<?> getAllBoardList(Pageable pageable){
        // 여기에 담긴 boardPage를 모두 조회, 조건 없음
        Page<Board> boardPage = boardRepository.showBoardPage(pageable);

        List<BoardResponseDTO> responseList =
                boardPage.map(board ->
                        BoardResponseDTO.builder()
                                .title(board.getTitle())
                                .description(board.getDescription())
                                .boardId(board.getId())
                                .memberId(board.getMember().getId())
                                .writer(board.getMember().getUsername())
                                .createAt(board.getCreatedAt())
                                .build()
                ).getContent();

        return new CommonResponseDto<>("모든 게시글 조회", 200, responseList);
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
