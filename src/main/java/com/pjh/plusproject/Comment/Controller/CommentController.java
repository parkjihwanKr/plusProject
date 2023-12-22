package com.pjh.plusproject.Comment.Controller;

import com.pjh.plusproject.Comment.DTO.CommentRequestDTO;
import com.pjh.plusproject.Comment.Service.CommentService;
import com.pjh.plusproject.Global.DTO.CommonResponseDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RequestMapping("/api/v1")
@RequiredArgsConstructor
@RestController
public class CommentController {

    private final CommentService commentService;

    @GetMapping("/board/{boardId}/comment")
    public ResponseEntity<CommonResponseDTO<?>> showBoardAllComment(@PathVariable long boardId){
        CommonResponseDTO<?> responseDto =  commentService.showBoardAllComment(boardId);
        return new ResponseEntity<>(responseDto, responseDto.getStatus().getHttpStatus());
    }

    @PostMapping("/board/{boardId}/comment")
    public ResponseEntity<CommonResponseDTO<?>> createComment(@PathVariable long boardId, CommentRequestDTO requestDTO){
        CommonResponseDTO<?> responseDto = commentService.createComment(boardId, requestDTO);
        return new ResponseEntity<>(responseDto, responseDto.getStatus().getHttpStatus());
    }

    @PutMapping("/board/comment/{commentId}")
    public ResponseEntity<CommonResponseDTO<?>> updateComment(@PathVariable long commentId, @RequestBody CommentRequestDTO requestDTO){
        // raw json으로 흘러 들어가는 것은 무조건 @RequestBody 작성
        CommonResponseDTO<?> responseDto = commentService.updateComment(commentId, requestDTO);
        return new ResponseEntity<>(responseDto, responseDto.getStatus().getHttpStatus());
    }

    @DeleteMapping("/board/comment/{commentId}")
    public ResponseEntity<CommonResponseDTO<?>> deleteComment(@PathVariable long commentId){
        CommonResponseDTO<?> responseDto = commentService.deleteComment(commentId);
        return new ResponseEntity<>(responseDto, responseDto.getStatus().getHttpStatus());
    }

}
