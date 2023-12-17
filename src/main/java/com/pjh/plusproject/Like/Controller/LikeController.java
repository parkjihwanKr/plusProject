package com.pjh.plusproject.Like.Controller;

import com.pjh.plusproject.Global.Common.CommonResponseDto;
import com.pjh.plusproject.Like.Service.LikeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
public class LikeController {

    private final LikeService likeService;
    @PostMapping("/board/{boardId}/like")
    public ResponseEntity<CommonResponseDto<?>> likeBoard(@PathVariable long boardId){
        CommonResponseDto<?> responseDto = likeService.likeBoard(boardId);
        return new ResponseEntity<>(responseDto, responseDto.getStatus().getHttpStatus());
    }

    @DeleteMapping("/board/{boardId}/like")
    public ResponseEntity<CommonResponseDto<?>> unlikeBoard(@PathVariable long boardId){
        CommonResponseDto<?> responseDto = likeService.unlikeBoard(boardId);
        return new ResponseEntity<>(responseDto, responseDto.getStatus().getHttpStatus());
    }

    @PostMapping("/member/{memberId}/like")
    public ResponseEntity<CommonResponseDto<?>> likeMember(@PathVariable long memberId){
        CommonResponseDto<?> responseDto = likeService.likeBoard(memberId);
        return new ResponseEntity<>(responseDto, responseDto.getStatus().getHttpStatus());
    }

    @DeleteMapping("/member/{memberId}/like")
    public ResponseEntity<CommonResponseDto<?>> unlikeMember(@PathVariable long memberId){
        CommonResponseDto<?> responseDto = likeService.unlikeBoard(memberId);
        return new ResponseEntity<>(responseDto, responseDto.getStatus().getHttpStatus());
    }
}
