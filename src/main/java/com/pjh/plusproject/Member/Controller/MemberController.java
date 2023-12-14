package com.pjh.plusproject.Member.Controller;

import com.pjh.plusproject.Global.Common.CommonResponseDto;
import com.pjh.plusproject.Member.DTO.SignupDTO;
import com.pjh.plusproject.Member.Service.MemberService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

@Slf4j(topic = "Member Controller")
@Controller
@RequiredArgsConstructor
@RequestMapping("/api/v1")
public class MemberController {
    private final MemberService memberService;
    @PostMapping("/signup")
    public ResponseEntity<CommonResponseDto<?>> signup(@Valid SignupDTO requestDto, BindingResult bindingResult){
        log.info("requestDto.getUsername() : "+requestDto.getUsername());
        if(bindingResult.hasErrors()){
            for(FieldError error : bindingResult.getFieldErrors()){
                log.error(error.getField()+ ": "+ error.getDefaultMessage());
                return new ResponseEntity<>(new CommonResponseDto<>("회원 가입 요청 실패", 400, error.getDefaultMessage()),HttpStatus.BAD_REQUEST);
            }
        }
        CommonResponseDto<?> commonResponseDto = memberService.signup(requestDto);
        return new ResponseEntity<>(commonResponseDto, HttpStatus.valueOf(commonResponseDto.getStatusCode()));
        // SignupDto 제약 조건 확인
    }

    // 로그인 버튼을 누른 경우 닉네임과 비밀번호가 데이터베이스에 등록됐는지 확인한 뒤,
    // 하나라도 맞지 않는 정보가 있다면 "닉네임 또는 패스워드를 확인해주세요."
    // 라는 에러 메세지를 response에 포함하기
    // 이 조건 때문에 controller에 적을지? 아니면 webSecurityConfig에서 처리를 할지?
}