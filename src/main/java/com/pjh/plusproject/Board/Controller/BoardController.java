package com.pjh.plusproject.Board.Controller;

import com.pjh.plusproject.Board.DTO.BoardRequestDTO;
import com.pjh.plusproject.Board.Service.BoardService;
import com.pjh.plusproject.Global.Common.CommonResponseDto;
import com.pjh.plusproject.Global.Security.MemberDetailsImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
public class BoardController {
    private final BoardService boardService;

    @GetMapping("/board")
    public ResponseEntity<CommonResponseDto<?>> showAllBoardList(){
            CommonResponseDto<?> responseDto = boardService.getAllBoardList();
        return new ResponseEntity<>(responseDto, HttpStatus.valueOf(responseDto.getStatusCode()));
    }

    @PostMapping("/board")
    public ResponseEntity<CommonResponseDto<?>> createBoard(BoardRequestDTO requestDTO, @AuthenticationPrincipal MemberDetailsImpl memberDetails){
        System.out.println("requestDTO.getTitle() : "+requestDTO.getTitle());
        System.out.println("requestDTO.getDescription() : "+requestDTO.getDescription());
        CommonResponseDto<?> responseDto = boardService.createBoard(requestDTO, memberDetails);
        return new ResponseEntity<>(responseDto, HttpStatus.valueOf(responseDto.getStatusCode()));
    }
}
