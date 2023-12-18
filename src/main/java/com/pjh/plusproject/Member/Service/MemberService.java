package com.pjh.plusproject.Member.Service;

import com.pjh.plusproject.Global.Common.CommonResponseDto;
import com.pjh.plusproject.Global.Exception.HttpStatusCode;
import com.pjh.plusproject.Member.DTO.SignupDTO;
import com.pjh.plusproject.Member.Entity.Member;
import com.pjh.plusproject.Member.Entity.MemberRoleEnum;
import com.pjh.plusproject.Member.Repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
public class MemberService {
    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
    public CommonResponseDto<?> signup(SignupDTO signupDTO) {
        // 지금 해당하는 Member에 안 들어가는 이유는 당연히, username의 중복성을 물어보는 것이기에
        // NoSuchElementException이 터지는게 맞네?
        // 해당하는 username이 중복되는지만 확인하고
        Member duplicateUsernameMember = memberRepository.findByUsername(signupDTO.getUsername()).orElse(null);
        // 해당하는 username이 중복될 때는?
        if(duplicateUsernameMember != null){
            throw new NoSuchElementException("중복되는 멤버 네임으로 작성하셨습니다. 다시 시도 해주세요.");
        }
        Member duplicateEmailMember = memberRepository.findByEmail(signupDTO.getEmail()).orElse(null);
        if(duplicateEmailMember != null){
            throw new NoSuchElementException("중복되는 멤버 이메일로 작성하셨습니다. 다시 시도 해주세요.");
        }
        // (챌린지 과제) 데이터베이스에 비밀번호를 평문으로 저장하는 것이 아닌, 단방향 암호화 알고리즘을 이용하여 암호화 해서 저장하도록 하기
        String bcrytPassword = passwordEncoder.encode(signupDTO.getPassword());

        Member member = Member.builder()
                .username(signupDTO.getUsername())
                .password(bcrytPassword)
                .email(signupDTO.getEmail())
                .role(MemberRoleEnum.USER)
                .build();
        memberRepository.save(member);
        return new CommonResponseDto<>("회원 가입에 성공하셨습니다.", HttpStatusCode.CREATED);
    }
}
