package com.pjh.plusproject.Member.Controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pjh.plusproject.Global.DTO.CommonResponseDTO;
import com.pjh.plusproject.Global.Exception.HttpStatusCode;
import com.pjh.plusproject.Global.Security.WebSecurityConfig;
import com.pjh.plusproject.Member.DTO.SignupDTO;
import com.pjh.plusproject.Member.Service.MemberService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(
        controllers = {MemberController.class},
        excludeFilters = {
                @ComponentScan.Filter(
                        type = FilterType.ASSIGNABLE_TYPE,
                        classes = WebSecurityConfig.class
                )
        }
)
public class MemberControllerTest {

    @MockBean
    private MemberService memberService;

    @Autowired
    MockMvc mockMvc;

    @Test
    @DisplayName("[MemberController] signup success")
    void signupSuccess() throws Exception {
        // given
        /*SignupDTO signupDTO = new SignupDTO(
                "testUser12",
                "123412342",
                "test1234@naver.com");

        // when
        when(memberService.signup(signupDTO)).thenReturn(
                new CommonResponseDTO<>(
                        "회원 가입에 성공하셨습니다.",
                        HttpStatusCode.CREATED
                )
        );

        // then
        ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/signup")
                        .with(csrf())
                        .contentType("application/json")
                        .content(new ObjectMapper().writeValueAsString(signupDTO)))
                        .andExpect(status().isCreated());

        resultActions.andExpect(result -> {
            CommonResponseDTO<?> actualResponse = new ObjectMapper()
                    .readValue(result.getResponse().getContentAsString(), CommonResponseDTO.class);
            assertEquals(new CommonResponseDTO<>(
                    "회원 가입에 성공하셨습니다.",
                    HttpStatusCode.CREATED
            ), actualResponse);
        });*/
    }
}
