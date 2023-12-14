package com.pjh.plusproject.Board.DTO;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Builder
public class BoardRequestDTO {
    private String title;
    private String description;

}
