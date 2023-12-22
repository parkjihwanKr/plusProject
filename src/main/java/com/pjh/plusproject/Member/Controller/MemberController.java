package com.pjh.plusproject.Member.Controller;

import com.pjh.plusproject.Global.DTO.CommonResponseDTO;
import com.pjh.plusproject.Member.DTO.LoginDTO;
import com.pjh.plusproject.Member.DTO.SignupDTO;
import com.pjh.plusproject.Member.Service.MemberService;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Slf4j(topic = "Member Controller")
@Controller
@RequiredArgsConstructor
@RequestMapping("/api/v1")
public class MemberController {
    private final MemberService memberService;

    @PostMapping("/signup")
    public ResponseEntity<CommonResponseDTO<?>> signup(@Valid SignupDTO signupDto){
        CommonResponseDTO<?> commonResponseDto = memberService.signup(signupDto);
        return new ResponseEntity<>(commonResponseDto, commonResponseDto.getStatus().getHttpStatus());
        // SignupDto 제약 조건 확인
    }

    @PostMapping("/login")
    public ResponseEntity<CommonResponseDTO<?>> login(@Valid @RequestBody LoginDTO loginDTO, HttpServletResponse res){
        CommonResponseDTO<?> commonResponseDto = memberService.login(loginDTO, res);
        return new ResponseEntity<>(commonResponseDto, commonResponseDto.getStatus().getHttpStatus());
    }
}