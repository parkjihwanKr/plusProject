package com.pjh.plusproject.Member.Controller;

import com.pjh.plusproject.Global.Common.CommonResponseDto;
import com.pjh.plusproject.Member.DTO.LoginDTO;
import com.pjh.plusproject.Member.DTO.SignupDTO;
import com.pjh.plusproject.Member.Service.MemberService;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import java.util.NoSuchElementException;

@Slf4j(topic = "Member Controller")
@Controller
@RequiredArgsConstructor
@RequestMapping("/api/v1")
public class MemberController {
    private final MemberService memberService;

    @PostMapping("/signup")
    public ResponseEntity<CommonResponseDto<?>> signup(@Valid SignupDTO signupDto){
        CommonResponseDto<?> commonResponseDto = memberService.signup(signupDto);
        return new ResponseEntity<>(commonResponseDto, commonResponseDto.getStatus().getHttpStatus());
        // SignupDto 제약 조건 확인
    }

    @PostMapping("/login")
    public ResponseEntity<CommonResponseDto<?>> login(@Valid @RequestBody LoginDTO loginDTO, HttpServletResponse res){
        CommonResponseDto<?> commonResponseDto = memberService.login(loginDTO, res);
        return new ResponseEntity<>(commonResponseDto, commonResponseDto.getStatus().getHttpStatus());
    }
}