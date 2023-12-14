package com.pjh.plusproject.Board.DTO;

import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Builder
public class BoardRequestDTO {

    @Size(max = 500, message = "Title은 500자 이내로 입력하세요.")
    private String title;

    @Size(max = 5000, message = "Description은 5000자 이내로 입력하세요.")
    private String description;

}
