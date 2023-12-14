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
        // 회원 가입 버튼을 누르기 전, 같은 닉네임이 존재하는지 "확인" 버튼을 눌러 먼저 유효성 검증부터 할 수 있도록 해보기
        if(duplicateUsernameMember != null){
            return new CommonResponseDto<>("해당 member의 username는 존재합니다.", 400);
        }

        Member duplicateEmailMember = memberRepository.findByEmail(signupDTO.getUsername()).orElse(null);
        if(duplicateEmailMember != null){
            return new CommonResponseDto<>("해당 member의 email은 존재합니다.", 400);
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
        return new CommonResponseDto<>("회원 가입에 성공하셨습니다.", 200);
    }
}
