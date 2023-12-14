package com.pjh.plusproject.Board.Controller;

import com.pjh.plusproject.Board.DTO.BoardRequestDTO;
import com.pjh.plusproject.Board.Service.BoardService;
import com.pjh.plusproject.Global.Common.CommonResponseDto;
import com.pjh.plusproject.Global.Security.MemberDetailsImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;


@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
public class BoardController {
    private final BoardService boardService;

    @GetMapping("/board")
    public ResponseEntity<CommonResponseDto<?>> showAllBoardList(
            @PageableDefault(size = 3) Pageable pageable){
            CommonResponseDto<?> responseDto = boardService.getAllBoardList(pageable);
        return new ResponseEntity<>(responseDto, HttpStatus.valueOf(responseDto.getStatusCode()));
    }

    @PostMapping("/board")
    public ResponseEntity<CommonResponseDto<?>> createBoard(
            @RequestParam("file")MultipartFile multipartFile,
            BoardRequestDTO requestDTO,
            @AuthenticationPrincipal MemberDetailsImpl memberDetails) throws IOException {
        System.out.println("requestDTO.getTitle() : "+requestDTO.getTitle());
        System.out.println("requestDTO.getDescription() : "+requestDTO.getDescription());
        CommonResponseDto<?> responseDto = boardService.createBoard(multipartFile, requestDTO, memberDetails);
        return new ResponseEntity<>(responseDto, HttpStatus.valueOf(responseDto.getStatusCode()));
    }
}
