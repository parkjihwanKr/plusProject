package com.pjh.plusproject.Member.DTO;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LoginDTO {

    @NotNull
    @Pattern(regexp = "^[a-zA-Z0-9]{3,}$", message = "username은 최소 3자 이상이며 알파벳 소문자(a~z), 숫자(0~9)로 구성되어야 합니다.")
    private String username;

    @NotNull
    @Pattern(regexp = "^[a-zA-Z0-9]{4,}$", message = "password는 최소 4자 이상이며 알파벳 대소문자(a~z, A~Z), 숫자(0~9)로 구성되어야 합니다.")
    private String password;
}
