package com.pjh.plusproject.Member.Service;

import com.pjh.plusproject.Global.Common.CommonResponseDto;
import com.pjh.plusproject.Member.DTO.SignupDTO;
import com.pjh.plusproject.Member.Entity.Member;
import com.pjh.plusproject.Member.Entity.MemberRoleEnum;
import com.pjh.plusproject.Member.Repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MemberService {
    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
    public CommonResponseDto<?> signup(SignupDTO signupDTO) {
        Member duplicateUsernameMember = memberRepository.findByUsername(signupDTO.getUsername()).orElse(null);
        if(duplicateUsernameMember != null){
            return new CommonResponseDto<>("해당 member의 id는 존재합니다.", 400);
        }

        Member duplicateEmailMember = memberRepository.findByEmail(signupDTO.getUsername()).orElse(null);
        if(duplicateEmailMember != null){
            return new CommonResponseDto<>("해당 member의 email은 존재합니다.", 400);
        }

        String bcrytPassword = passwordEncoder.encode(signupDTO.getPassword());
        Member member = Member.builder()
                .username(signupDTO.getUsername())
                .password(bcrytPassword)
                .email(signupDTO.getEmail())
                .role(MemberRoleEnum.USER)
                .build();
        memberRepository.save(member);
        return new CommonResponseDto<>("회원 가입에 성공하셨습니다.", 200);
    }
}
