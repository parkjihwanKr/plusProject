package com.pjh.plusproject.Like.Controller;

import com.pjh.plusproject.Global.DTO.CommonResponseDTO;
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
    public ResponseEntity<CommonResponseDTO<?>> likeBoard(@PathVariable long boardId){
        CommonResponseDTO<?> responseDto = likeService.likeBoard(boardId);
        return new ResponseEntity<>(responseDto, responseDto.getStatus().getHttpStatus());
    }

    @DeleteMapping("/board/{boardId}/like")
    public ResponseEntity<CommonResponseDTO<?>> unlikeBoard(@PathVariable long boardId){
        CommonResponseDTO<?> responseDto = likeService.unlikeBoard(boardId);
        return new ResponseEntity<>(responseDto, responseDto.getStatus().getHttpStatus());
    }

    @PostMapping("/member/{memberId}/like")
    public ResponseEntity<CommonResponseDTO<?>> likeMember(@PathVariable long memberId){
        CommonResponseDTO<?> responseDto = likeService.likeMember(memberId);
        return new ResponseEntity<>(responseDto, responseDto.getStatus().getHttpStatus());
    }

    @DeleteMapping("/member/{memberId}/like")
    public ResponseEntity<CommonResponseDTO<?>> unlikeMember(@PathVariable long memberId){
        CommonResponseDTO<?> responseDto = likeService.unlikeMember(memberId);
        return new ResponseEntity<>(responseDto, responseDto.getStatus().getHttpStatus());
    }
}
