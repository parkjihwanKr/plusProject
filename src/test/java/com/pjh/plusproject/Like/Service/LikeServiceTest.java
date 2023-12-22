package com.pjh.plusproject.Like.Service;

import com.pjh.plusproject.Board.Entity.Board;
import com.pjh.plusproject.Board.Repository.BoardRepository;
import com.pjh.plusproject.Global.DTO.CommonResponseDTO;
import com.pjh.plusproject.Global.Exception.HttpStatusCode;
import com.pjh.plusproject.Like.DTO.LikeResponseDTO;
import com.pjh.plusproject.Like.Repository.LikeRepository;
import com.pjh.plusproject.Member.Entity.Member;
import com.pjh.plusproject.Member.Repository.MemberRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.context.ActiveProfiles;

import java.util.NoSuchElementException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ActiveProfiles("test")
@ExtendWith(MockitoExtension.class)
public class LikeServiceTest {

    @Mock
    private LikeRepository likeRepository;

    @Mock
    private BoardRepository boardRepository;

    @Mock
    private MemberRepository memberRepository;

    @InjectMocks
    private LikeService likeService;

    @Test
    @DisplayName("[LikeService] likeBoard success")
    void likeBoardSuccess() {
        // given
        long boardId = 1L;

        // MockWithUser 어노테이션은 Controller에서 사용하는 걸로
        String loginMemberName = "testUser";

        Board board = Board.builder()
                .id(boardId)
                .title("Test Title")
                .description("Test Description")
                .member(Member.builder().username("author").build())
                .build();

        when(boardRepository.findById(boardId)).thenReturn(Optional.of(board));
        when(likeRepository.existsByBoardIdAndFromMemberId(boardId, board.getMember().getId())).thenReturn(false);

        Authentication authentication = mock(Authentication.class);
        when(authentication.getName()).thenReturn(loginMemberName);

        SecurityContext securityContext = mock(SecurityContext.class);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);

        // when
        CommonResponseDTO<?> result = likeService.likeBoard(boardId);

        // then
        assertNotNull(result);
        assertEquals(HttpStatusCode.OK, result.getStatus());
        assertNotNull(result.getData());

        LikeResponseDTO likeResponseDTO = (LikeResponseDTO) result.getData();
        assertEquals("testUser", likeResponseDTO.getFromMemberName());
        assertEquals("author", likeResponseDTO.getToMemberName());
        assertEquals("Test Title", likeResponseDTO.getBoardTitle());
        assertEquals("Test Description", likeResponseDTO.getBoardDescription());
    }

    @Test
    @DisplayName("[LikeService] likeBoard not exist board id")
    void likeBoardNotExistBoard() {
        // given
        long boardId = 1L;

        // 해당 게시글이 존재하지 않기에 when 작성필요 없음
        when(boardRepository.findById(boardId)).thenReturn(Optional.empty());

        // when, then
        assertThrows(NoSuchElementException.class, () -> likeService.likeBoard(boardId));
    }

    @Test
    @DisplayName("[LikeService] likeBoard memberId equals board.memberId")
    void likeBoardtoMemberequalBoardMemberId(){
        // given

        // '좋아요'를 누르는 거는 일단 login해놔야함
        // 해당 게시글의 작성자가 자신이면 board에 좋아요를 누를 수 없다.

        String loginMemberName = "testUser";

        Member boardWriter = Member.builder()
                .username("testUser")
                .password("password")
                .build();

        Board board = Board.builder()
                .id(1L)
                .title("title1")
                .description("description1")
                .member(boardWriter)
                .build();

        when(boardRepository.findById(board.getId())).thenReturn(Optional.of(board));

        Authentication authentication = mock(Authentication.class);
        when(authentication.getName()).thenReturn(loginMemberName);

        SecurityContext securityContext = mock(SecurityContext.class);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);

        // when, then
        assertThrows(IllegalArgumentException.class, () -> likeService.likeBoard(board.getId()));
    }

    @Test
    @DisplayName("[LikeService] likeMember Success")
    void likeMemberSuccess() {
        // given
        long toMemberId = 1L;
        String loginMemberName = "testUser";

        // toMember
        Member toMember = Member.builder()
                .id(toMemberId)
                .username("username")
                .password("password")
                .build();

        // fromMember
        Member loginMember = Member.builder()
                .id(3L)
                .username(loginMemberName)
                .password("password")
                .build();

        when(memberRepository.findById(toMemberId)).thenReturn(Optional.of(toMember));
        when(memberRepository.findByUsername(loginMemberName)).thenReturn(Optional.of(loginMember));
        when(likeRepository.existsByToMemberIdAndFromMemberId(loginMember.getId(), toMemberId)).thenReturn(false);

        Authentication authentication = mock(Authentication.class);
        when(authentication.getName()).thenReturn(loginMemberName);

        SecurityContext securityContext = mock(SecurityContext.class);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);

        // when
        CommonResponseDTO<?> result = likeService.likeMember(toMemberId);

        // then
        assertEquals("해당 멤버 좋아요 성공", result.getMessage());
        assertEquals(HttpStatus.OK, result.getStatus().getHttpStatus());

        LikeResponseDTO likeResponseDTO = (LikeResponseDTO) result.getData();
        assertNotNull(likeResponseDTO);
        assertEquals(loginMemberName, likeResponseDTO.getFromMemberName());
        assertEquals(toMember.getUsername(), likeResponseDTO.getToMemberName());
    }

    @Test
    @DisplayName("[LikeService] Member not found")
    void likeMemberMemberNotFound() {
        // given
        long toMemberId = 1L;

        when(memberRepository.findById(toMemberId)).thenReturn(Optional.empty());
        // when
        assertThrows(NoSuchElementException.class, () -> likeService.likeMember(toMemberId));
    }

    @Test
    @DisplayName("[LikeService] login Member not found")
    void likeMemberloginMemberNotFound() {
        // given
        long toMemberId = 1L;

        // Mocking: 해당하는 toMember는 찾지만 로그인한 멤버를 찾지 못하도록 설정
        Member toMember = Member.builder()
                .id(toMemberId)
                .username("toMemberUsername")
                .password("password")
                .build();
        when(memberRepository.findById(toMemberId)).thenReturn(Optional.of(toMember));
        when(memberRepository.findByUsername(anyString())).thenReturn(Optional.empty());

        // when
        assertThrows(NoSuchElementException.class, () -> likeService.likeMember(toMemberId));
    }

    @Test
    @DisplayName("[LikeService] already Liked")
    void likeMemberAlreadyLiked() {
        // given
        long toMemberId = 1L;

        // Mocking: 이미 좋아요를 누른 상태로 설정
        Member toMember = Member.builder()
                .id(toMemberId)
                .username("toMemberUsername")
                .password("password")
                .build();
        Member loggedInMember = Member.builder()
                .id(3L)
                .username("testUser")
                .password("password")
                .build();
        when(memberRepository.findById(toMemberId)).thenReturn(Optional.of(toMember));
        when(memberRepository.findByUsername(anyString())).thenReturn(Optional.of(loggedInMember));
        when(likeRepository.existsByToMemberIdAndFromMemberId(loggedInMember.getId(), toMemberId)).thenReturn(true);

        // when
        assertThrows(IllegalArgumentException.class, () -> likeService.likeMember(toMemberId));
    }
}
