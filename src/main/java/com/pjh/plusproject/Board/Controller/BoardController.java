package com.pjh.plusproject.Board.Controller;

import com.pjh.plusproject.Board.DTO.BoardRequestDTO;
import com.pjh.plusproject.Board.Service.BoardService;
import com.pjh.plusproject.Global.DTO.CommonResponseDTO;
import com.pjh.plusproject.Global.Security.MemberDetailsImpl;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
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
    public ResponseEntity<CommonResponseDTO<?>> showAllBoardList(
            @PageableDefault(size = 3) Pageable pageable){
            CommonResponseDTO<?> responseDto = boardService.showAllBoardList(pageable);
        return new ResponseEntity<>(responseDto, responseDto.getStatus().getHttpStatus());
    }

    @GetMapping("/board/{boardId}")
    public ResponseEntity<CommonResponseDTO<?>> showBoard(
            @PathVariable long boardId){
        CommonResponseDTO<?> responseDto = boardService.showBoard(boardId);
        return new ResponseEntity<>(responseDto, responseDto.getStatus().getHttpStatus());
    }

    @GetMapping("/member/board/{memberId}")
    public ResponseEntity<CommonResponseDTO<?>> showMemberBoardList(
            @PathVariable long memberId){
        CommonResponseDTO<?> responseDto = boardService.showMemberBoard(memberId);
        return new ResponseEntity<>(responseDto, responseDto.getStatus().getHttpStatus());
    }

    @PostMapping("/board")
    public ResponseEntity<CommonResponseDTO<?>> createBoard(
            @RequestParam("file")MultipartFile multipartFile,
            @Valid BoardRequestDTO requestDTO,
            @AuthenticationPrincipal MemberDetailsImpl memberDetails) throws IOException {
        CommonResponseDTO<?> responseDto = boardService.createBoard(multipartFile, requestDTO, memberDetails);
        return new ResponseEntity<>(responseDto, responseDto.getStatus().getHttpStatus());
    }

    @PutMapping("/board/{boardId}")
    public ResponseEntity<CommonResponseDTO<?>> updateBoard(
            @RequestParam("file")MultipartFile multipartFile,
            @PathVariable long boardId,
            @Valid BoardRequestDTO requestDTO,
            @AuthenticationPrincipal MemberDetailsImpl memberDetails) throws IOException{
        CommonResponseDTO<?> responseDto = boardService.updateBoard(multipartFile, boardId, requestDTO, memberDetails);
        return new ResponseEntity<>(responseDto,responseDto.getStatus().getHttpStatus());
    }

    @DeleteMapping("/board/{boardId}")
    public ResponseEntity<?> deleteBoard(@PathVariable long boardId){
        CommonResponseDTO<?> responseDto = boardService.deleteBoard(boardId);
        return new ResponseEntity<>(responseDto, responseDto.getStatus().getHttpStatus());
    }
}
