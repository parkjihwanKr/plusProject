package com.pjh.plusproject.Global.DTO;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class TokenDTO {
    private final String accessToken;
    private final String refreshToken;

    public static TokenDTO of(String accessToken, String refreshToken){
        return new TokenDTO(accessToken,refreshToken);
    }
}
