package com.pjh.plusproject.Comment.Service;

import com.pjh.plusproject.Board.Entity.Board;
import com.pjh.plusproject.Board.Repository.BoardRepository;
import com.pjh.plusproject.Comment.DTO.CommentRequestDTO;
import com.pjh.plusproject.Comment.Repository.CommentRepository;
import com.pjh.plusproject.Global.DTO.CommonResponseDTO;
import com.pjh.plusproject.Member.Entity.Member;
import com.pjh.plusproject.Member.Repository.MemberRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.context.SecurityContextImpl;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;

import java.util.NoSuchElementException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ActiveProfiles("test")
@ExtendWith(MockitoExtension.class)
class CommentServiceTest {

    @Mock
    private CommentRepository commentRepository;

    @Mock
    private BoardRepository boardRepository;

    @Mock
    private MemberRepository memberRepository;

    @InjectMocks
    private CommentService commentService;

    @Test
    @DisplayName("[CommentService] createComment success")
    @WithMockUser(username = "testUser")
    void createCommentSuccess(){
        // given
        CommentRequestDTO requestDTO = new CommentRequestDTO("content");

        Member member = Member.builder()
                .username("testUser")
                .password("password").build();

        Board board = Board.builder()
                .id(1L)
                .title("title1")
                .description("description")
                .member(member)
                .build();

        when(boardRepository.findById(board.getId())).thenReturn(Optional.of(board));
        when(memberRepository.findByUsername(anyString())).thenReturn(Optional.of(member));

        Authentication authentication = new UsernamePasswordAuthenticationToken(
                member.getUsername(),
                member.getPassword(),
                null
        );

        SecurityContext securityContext = new SecurityContextImpl();
        securityContext.setAuthentication(authentication);
        SecurityContextHolder.setContext(securityContext);

        // when

        CommonResponseDTO<?> result = commentService.createComment(board.getId(), requestDTO);

        // then
        assertEquals(HttpStatus.OK, result.getStatus().getHttpStatus());
        assertEquals("해당 게시글 댓글 작성 성공",result.getMessage());
        // assertEquals(responseDTO,result.getData());
    }

    @Test
    @DisplayName("[CommentService] createComment not found Board Id")
    @WithMockUser(username = "testUser")
    void createCommentNotFoundBoardId(){
        // given
        CommentRequestDTO requestDTO = new CommentRequestDTO("content");

        Member member = Member.builder()
                .username("testUser")
                .password("password").build();

        Board board = Board.builder()
                .id(1L)
                .title("title1")
                .description("description")
                .member(member)
                .build();

        Authentication authentication = new UsernamePasswordAuthenticationToken(
                member.getUsername(),
                member.getPassword(),
                null
        );

        SecurityContext securityContext = new SecurityContextImpl();
        securityContext.setAuthentication(authentication);
        SecurityContextHolder.setContext(securityContext);

        // when, then
        assertThrows(NoSuchElementException.class, ()-> commentService.createComment(999L, requestDTO));
    }
}
