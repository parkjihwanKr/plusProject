package com.pjh.plusproject.Member.Service;

import com.pjh.plusproject.Global.Common.CommonResponseDto;
import com.pjh.plusproject.Member.DTO.SignupDTO;
import com.pjh.plusproject.Member.Entity.Member;
import com.pjh.plusproject.Member.Repository.MemberRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;

import java.util.NoSuchElementException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ActiveProfiles("test")
@ExtendWith(MockitoExtension.class)
public class MemberServiceTest {

    @Mock
    private MemberRepository memberRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private MemberService memberService;

    @Test
    @DisplayName("[MemberService] signup Success")
    public void signupSuccess(){
        // given
        SignupDTO signupDTO = new SignupDTO("newUser", "12341234", "test1234@naver.com");

        when(memberRepository.findByUsername(signupDTO.getUsername())).thenReturn(Optional.empty());
        when(memberRepository.findByEmail(signupDTO.getEmail())).thenReturn(Optional.empty());
        // 해당 비밀번호는 rawPassword에서 encodedPassword로 변경되었다고 가정
        when(passwordEncoder.encode(signupDTO.getPassword())).thenReturn("encodedPassword");

        // when
        CommonResponseDto<?> response = memberService.signup(signupDTO);

        // then
        assertEquals("회원 가입에 성공하셨습니다.", response.getMessage());
        assertEquals(HttpStatus.CREATED, response.getStatus().getHttpStatus());
    }

    @Test
    @DisplayName("[MemberService] signup duplicate Member name")
    public void signupDuplicateMemberName(){
        // given
        SignupDTO signupDTO1 = new SignupDTO("duplicateUser", "12341234", "test1234@naver.com");
        SignupDTO signupDTO2 = new SignupDTO("duplicateUser", "12341234", "test12341@naver.com");

        when(memberRepository.findByUsername(signupDTO1.getUsername())).thenReturn(Optional.empty());
        when(memberRepository.findByEmail(signupDTO1.getEmail())).thenReturn(Optional.empty());

        // when 1
        CommonResponseDto<?> response1 = memberService.signup(signupDTO1);

        // then 1
        assertEquals("회원 가입에 성공하셨습니다.", response1.getMessage());
        assertEquals(HttpStatus.CREATED, response1.getStatus().getHttpStatus());

        // given
        when(memberRepository.findByUsername(signupDTO2.getUsername())).thenReturn(Optional.of(new Member()));

        // when 2
        assertThrows(NoSuchElementException.class, () -> memberService.signup(signupDTO2));
    }

    @Test
    @DisplayName("[MemberService] signup duplicate email")
    public void signupDuplicateEmail(){
        // given
        SignupDTO signupDTO1 = new SignupDTO("newUser1", "12341234", "duplicateEmail@naver.com");
        SignupDTO signupDTO2 = new SignupDTO("newUser2", "12341234", "duplicateEmail@naver.com");

        when(memberRepository.findByUsername(signupDTO1.getUsername())).thenReturn(Optional.empty());
        when(memberRepository.findByEmail(signupDTO1.getEmail())).thenReturn(Optional.empty());

        // when 1
        CommonResponseDto<?> response1 = memberService.signup(signupDTO1);

        // then 1
        assertEquals("회원 가입에 성공하셨습니다.", response1.getMessage());
        assertEquals(HttpStatus.CREATED, response1.getStatus().getHttpStatus());

        // given
        when(memberRepository.findByUsername(signupDTO2.getUsername())).thenReturn(Optional.of(new Member()));

        // when 2
        assertThrows(NoSuchElementException.class, () -> memberService.signup(signupDTO2));
    }
}
