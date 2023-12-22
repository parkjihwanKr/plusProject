package com.pjh.plusproject.Member.Service;

import com.pjh.plusproject.Global.Common.CommonResponseDto;
import com.pjh.plusproject.Global.DTO.TokenDTO;
import com.pjh.plusproject.Global.Exception.HttpStatusCode;
import com.pjh.plusproject.Global.Jwt.JwtProvider;
import com.pjh.plusproject.Global.Jwt.RedisProvider;
import com.pjh.plusproject.Global.Security.MemberDetailsImpl;
import com.pjh.plusproject.Member.DTO.LoginDTO;
import com.pjh.plusproject.Member.DTO.SignupDTO;
import com.pjh.plusproject.Member.Entity.Member;
import com.pjh.plusproject.Member.Entity.MemberRoleEnum;
import com.pjh.plusproject.Member.Repository.MemberRepository;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.NoSuchElementException;

@Service
@Slf4j(topic = "MemberService")
@RequiredArgsConstructor
public class MemberService {
    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtProvider jwtProvider;
    private final RedisProvider redisProvider;
    public CommonResponseDto<?> signup(SignupDTO signupDTO) {
        // 해당하는 username이 중복되는지만 확인
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

    @Transactional
    public CommonResponseDto<?> login(LoginDTO loginDTO, HttpServletResponse res) {
        Member memberEntity = memberRepository.findByUsername(loginDTO.getUsername()).orElseThrow();
        // 인증 객체 생성
        Authentication authentication = createAuthentication(memberEntity);
        setAuthentication(authentication);

        // login시에 token을 만들어서 넣어줌
        TokenDTO tokenDTO = jwtProvider.createToken(memberEntity.getUsername(), memberEntity.getRole());
        // 이미 여기서 tokenValue만 남는데?
        log.info("accessToken : "+tokenDTO.getAccessToken());
        log.info("refreshToken : "+tokenDTO.getRefreshToken());

        // 쿠키를 넣어주고 Header에 accessToken과 refreshToken을 넣어줌
        jwtProvider.setTokenForCookie(tokenDTO, res);
        res.addHeader(jwtProvider.getAuthorizationHeader(), tokenDTO.getAccessToken());
        res.addHeader(jwtProvider.getRefreshTokenHeader(), tokenDTO.getRefreshToken());

        // redis에 refershToken 저장하는 로직
        String key = "refreshToken : "+ memberEntity.getUsername();
        // redis에는 key-value 형식으로 출력
        redisProvider.saveKey(key, Math.toIntExact(jwtProvider.getRefreshTokenExpiration() / 1000),tokenDTO.getRefreshToken());

        return new CommonResponseDto<>("로그인에 성공하셨습니다.", HttpStatusCode.OK);
    }

    private Authentication createAuthentication(Member member){
        MemberDetailsImpl memberDetails = new MemberDetailsImpl(member);
        return new UsernamePasswordAuthenticationToken(memberDetails, member.getPassword(), memberDetails.getAuthorities());
    }

    private void setAuthentication(Authentication authentication){
        SecurityContext securityContext = SecurityContextHolder.getContext();
        securityContext.setAuthentication(authentication);
    }
}
