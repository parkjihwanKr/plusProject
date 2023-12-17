package com.pjh.plusproject.Board.Controller;

import com.pjh.plusproject.Board.DTO.BoardRequestDTO;
import com.pjh.plusproject.Board.DTO.BoardResponseDTO;
import com.pjh.plusproject.Board.Service.BoardService;
import com.pjh.plusproject.Global.Common.CommonResponseDto;
import com.pjh.plusproject.Global.Exception.HttpStatusCode;
import com.pjh.plusproject.Global.Security.MemberDetailsImpl;
import jakarta.validation.Valid;
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
            CommonResponseDto<?> responseDto = boardService.showAllBoardList(pageable);
        return new ResponseEntity<>(responseDto, responseDto.getStatus().getHttpStatus());
    }

    @GetMapping("/board/{boardId}")
    public ResponseEntity<CommonResponseDto<?>> showBoard(
            @PathVariable long boardId){
        CommonResponseDto<?> responseDto = boardService.showBoard(boardId);
        return new ResponseEntity<>(responseDto, responseDto.getStatus().getHttpStatus());
    }

    @GetMapping("/member/board/{memberId}")
    public ResponseEntity<CommonResponseDto<?>> showMemberBoardList(
            @PathVariable long memberId){
        CommonResponseDto<?> responseDto = boardService.showMemberBoard(memberId);
        return new ResponseEntity<>(responseDto, responseDto.getStatus().getHttpStatus());
    }

    @PostMapping("/board")
    public ResponseEntity<CommonResponseDto<?>> createBoard(
            @RequestParam("file")MultipartFile multipartFile,
            @Valid BoardRequestDTO requestDTO,
            @AuthenticationPrincipal MemberDetailsImpl memberDetails) throws IOException {
        CommonResponseDto<?> responseDto = boardService.createBoard(multipartFile, requestDTO, memberDetails);
        return new ResponseEntity<>(responseDto, responseDto.getStatus().getHttpStatus());
    }

    @PutMapping("/board/{boardId}")
    public ResponseEntity<CommonResponseDto<?>> updateBoard(
            @PathVariable long boardId,
            @Valid @RequestBody BoardRequestDTO requestDTO,
            @AuthenticationPrincipal MemberDetailsImpl memberDetails){
        CommonResponseDto<?> responseDto = boardService.updateBoard(boardId, requestDTO, memberDetails);
        return new ResponseEntity<>(responseDto,responseDto.getStatus().getHttpStatus());
    }

    @DeleteMapping("/board/{boardId}")
    public ResponseEntity<?> deleteBoard(@PathVariable long boardId){
        CommonResponseDto<?> responseDto = boardService.deleteBoard(boardId);
        return new ResponseEntity<>(responseDto, responseDto.getStatus().getHttpStatus());
    }
}
