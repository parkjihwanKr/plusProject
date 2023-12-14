package com.pjh.plusproject.Board.DTO;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Builder
public class BoardRequestDTO {

    @NotBlank(message = "Title은 필수 입력입니다.")
    @Size(max = 500)
    private String title;

    @Size(max = 5000, message = "Description은 5000자 이내로 입력하세요.")
    private String description;
}
