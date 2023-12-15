package com.pjh.plusproject.Comment.Controller;

import com.pjh.plusproject.Comment.DTO.CommentRequestDTO;
import com.pjh.plusproject.Comment.Service.CommentService;
import com.pjh.plusproject.Global.Common.CommonResponseDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RequestMapping("/api/v1")
@RequiredArgsConstructor
@RestController
public class CommentController {

    private final CommentService commentService;

    @PostMapping("/board/{boardId}/comment")
    public ResponseEntity<CommonResponseDto<?>> createComment(@PathVariable long boardId, CommentRequestDTO requestDTO){
        log.info("comment controller 진입");
        CommonResponseDto<?> responseDto = commentService.createComment(boardId, requestDTO);
        return new ResponseEntity<>(responseDto, HttpStatusCode.valueOf(responseDto.getStatusCode()));
    }

    @PutMapping("/board/comment/{commentId}")
    public ResponseEntity<CommonResponseDto<?>> updateComment(@PathVariable long commentId, CommentRequestDTO requestDTO){
        CommonResponseDto<?> responseDto = commentService.updateComment(commentId, requestDTO);
        return new ResponseEntity<>(responseDto, HttpStatusCode.valueOf(responseDto.getStatusCode()));
    }

    @DeleteMapping("/board/comment/{commentId}")
    public ResponseEntity<CommonResponseDto<?>> deleteComment(@PathVariable long commentId){
        CommonResponseDto<?> responseDto = commentService.deleteComment(commentId);
        return new ResponseEntity<>(responseDto, HttpStatusCode.valueOf(responseDto.getStatusCode()));
    }
}
