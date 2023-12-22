package com.pjh.plusproject.Board.Service;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.PutObjectResult;
import com.pjh.plusproject.Board.DTO.BoardRequestDTO;
import com.pjh.plusproject.Board.DTO.BoardResponseDTO;
import com.pjh.plusproject.Board.Entity.Board;
import com.pjh.plusproject.Board.Repository.BoardRepository;
import com.pjh.plusproject.Global.Common.CommonResponseDto;
import com.pjh.plusproject.Global.Exception.HttpStatusCode;
import com.pjh.plusproject.Global.Security.MemberDetailsImpl;
import com.pjh.plusproject.Member.Entity.Member;
import com.pjh.plusproject.Member.Entity.MemberRoleEnum;
import com.pjh.plusproject.Member.Repository.MemberRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ActiveProfiles("test")
@ExtendWith(MockitoExtension.class)
public class BoardServiceTest {
    @Mock
    private BoardRepository boardRepository;
    @Mock
    private MemberRepository memberRepository;
    @InjectMocks
    private BoardService boardService;
    @Mock
    private AmazonS3 amazonS3;

    @Test
    @DisplayName("[BoardService] createBoard succes")
    public void createBoardSuccess() throws IOException{
        // given
        BoardService boardService = new BoardService(boardRepository, memberRepository, amazonS3);
        MemberDetailsImpl memberDetails = createMockMemberDetails();
        BoardRequestDTO boardRequestDTO = createMockBoardRequestDTO();
        MultipartFile image = createMockImage();

        // when
        when(amazonS3.putObject(any(), any(), any(), any())).thenReturn(null);

        CommonResponseDto<?> response = boardService.createBoard(image, boardRequestDTO, memberDetails);

        // then
        assertEquals("게시글 작성 성공", response.getMessage());
        assertEquals(HttpStatus.CREATED, response.getStatus().getHttpStatus());
    }

    @Test
    @DisplayName("[BoardService] createBoard IOException")
    public void createBoardIOException() throws IOException {
        // given
        BoardService boardService = new BoardService(boardRepository, memberRepository, amazonS3);
        MemberDetailsImpl memberDetails = createMockMemberDetails();
        BoardRequestDTO boardRequestDTO = createMockBoardRequestDTO();

        // Mock MultipartFile
        MultipartFile mockImage = mock(MultipartFile.class);

        when(mockImage.getInputStream()).thenThrow(java.io.IOException.class);

        // when
        // 해당 예외가 발생하는 부분을 assertThrows를 사용하여 확인
        IOException exception = assertThrows(IOException.class,
                () -> boardService.createBoard(mockImage, boardRequestDTO, memberDetails));

        // then
        assertEquals("해당 이미지 파일은 잘못된 형식입니다.", exception.getMessage());
    }

    @Test
    @DisplayName("[BoardService] showAllBoardList Success")
    public void showAllBoardListSuccess(){
        // given
        Pageable pageable = PageRequest.of(0, 3); // 예시로 페이지 크기를 3으로 설정

        Board board1 = Board.builder()
                .title("title")
                .description("description")
                .member(Member.builder()
                        .username("user1")
                        .build())
                .build();

        Board board2 = Board.builder()
                .title("title")
                .description("description")
                .member(Member.builder()
                        .username("user2")
                        .build())
                .build();

        Board board3 = Board.builder()
                .title("title")
                .description("description")
                .member(Member.builder()
                        .username("user3")
                        .build())
                .build();

        Board board4 = Board.builder()
                .title("title")
                .description("description")
                .member(Member.builder()
                        .username("user4")
                        .build())
                .build();

        List<Board> boardList = new ArrayList<>();

        boardList.add(board1);
        boardList.add(board2);
        boardList.add(board3);
        boardList.add(board4);

        Page<Board> mockBoardPage = new PageImpl<>(boardList, pageable, boardList.size());

        // boardRepository.showBoardPage(pageable)가 호출될 때 mockBoardPage를 반환하도록 설정
        when(boardRepository.showBoardPage(pageable)).thenReturn(mockBoardPage);

        // when
        CommonResponseDto<?> response = boardService.showAllBoardList(pageable);

        // then
        assertEquals("모든 게시글 조회", response.getMessage());
        assertEquals(HttpStatus.OK, response.getStatus().getHttpStatus());
    }

    @Test
    @DisplayName("[BoardService] showMemberBoard Suceess")
    public void showMemberBoardSuceess() {
        // given
        long memberId = 1L; // 예시로 멤버 ID를 1로 설정

        // Mock 데이터 생성
        Member member = createMockMember();
        List<Board> memberBoardList = createMockMemberBoardList();

        // memberRepository.findById(memberId)가 호출될 때 mockMember를 반환하도록 설정
        when(memberRepository.findById(memberId)).thenReturn(Optional.of(member));

        // boardRepository.findAllByMemberId(memberId)가 호출될 때 mockMemberBoardList를 반환하도록 설정
        when(boardRepository.findAllByMemberId(memberId)).thenReturn(memberBoardList);

        // when
        CommonResponseDto<?> response = boardService.showMemberBoard(memberId);

        // then
        assertEquals("해당 멤버의 모든 게시글 조회 성공", response.getMessage());
        assertEquals(HttpStatus.OK, response.getStatus().getHttpStatus());

        // memberRepository.findById(memberId)가 1번 호출되었는지 검증
        verify(memberRepository, times(1)).findById(memberId);

        // boardRepository.findAllByMemberId(memberId)가 1번 호출되었는지 검증
        verify(boardRepository, times(1)).findAllByMemberId(memberId);
    }

    private MemberDetailsImpl createMockMemberDetails(){
        return new MemberDetailsImpl(
                Member.builder()
                        .username("loginUsername")
                        .password("12341234")
                        .build()
        );
    }

    private BoardRequestDTO createMockBoardRequestDTO(){
        return BoardRequestDTO.builder()
                .title("title")
                .description("description")
                .build();
    }

    private MultipartFile createMockImage() {
        String content = "Mock Image Content";
        byte[] contentBytes = content.getBytes(StandardCharsets.UTF_8);

        return new MockMultipartFile("image", "mock_image.jpg", "image/jpeg", contentBytes);
    }

    private Member createMockMember(){
        return Member.builder()
                .id(1L)
                .username("user1")
                .build();
    }

    private List<Board> createMockMemberBoardList(){
        List<Board> memberList = new ArrayList<>();
        for(int i = 0; i< 5; i++){
            Board board = Board.builder()
                    .title("title"+i)
                    .description("description"+i)
                    .member(createMockMember())
                    .build();
            memberList.add(board);
        }
        return memberList;
    }
}
