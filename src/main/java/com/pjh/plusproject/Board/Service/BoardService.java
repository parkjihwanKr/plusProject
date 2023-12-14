package com.pjh.plusproject.Board.Service;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.pjh.plusproject.Board.DTO.BoardRequestDTO;
import com.pjh.plusproject.Board.DTO.BoardResponseDTO;
import com.pjh.plusproject.Board.Entity.Board;
import com.pjh.plusproject.Board.Repository.BoardRepository;
import com.pjh.plusproject.Global.Common.CommonResponseDto;
import com.pjh.plusproject.Global.Security.MemberDetailsImpl;
import com.pjh.plusproject.Member.Entity.Member;
import com.pjh.plusproject.Member.Repository.MemberRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Slf4j
@Service
public class BoardService {
    private final BoardRepository boardRepository;
    private final MemberRepository memberRepository;
    private final AmazonS3 amazonS3;

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    public BoardService(BoardRepository boardRepository, MemberRepository memberRepository, AmazonS3 amazonS3){
        this.boardRepository = boardRepository;
        this.memberRepository = memberRepository;
        this.amazonS3 = amazonS3;
    }

    public CommonResponseDto<?> createBoard(
            MultipartFile image,
            BoardRequestDTO requestDTO,
            MemberDetailsImpl memberDetails){
        log.info("Service method start!");
        String uuidImageName = null;

        try {
            String uuid = UUID.randomUUID().toString();
            String originalImageName = image.getOriginalFilename();

            uuidImageName = uuid+"_"+originalImageName;

            ObjectMetadata metaData = new ObjectMetadata();
            metaData.setContentType(image.getContentType());
            metaData.setContentLength(image.getSize());
            amazonS3.putObject(bucket, uuidImageName, image.getInputStream(), metaData);

        }catch (IOException e){
            e.printStackTrace();
            return new CommonResponseDto<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR.value(), null);
        }

        Board boardEntity = Board.builder()
                .title(requestDTO.getTitle())
                .description(requestDTO.getDescription())
                .member(memberDetails.getMember())
                .imageUrl(uuidImageName)
                .build();

        boardRepository.save(boardEntity);
        BoardResponseDTO responseDTO = boardEntity.showBoard(boardEntity);
        return new CommonResponseDto<>("게시글 작성 성공", 200, responseDTO);
    }

    // Page<BoardResponseDto> reponseList 형태로 repository 접근은 좋지 않음
    // 이유 : 역할의 분리 측면에서 좋지 않음
    // Presentation layer와 Service layer의 역할 분리, 변경의 유연성
    // Presentation layer는 데이터 전송에 역할을 맞춰야함
    // Service layer은 비지니스 로직의 역할에 충실해야함
    // 그러므로 Persistentce layer(repository)에 Dto형태가 들어가는 것은 말이 안됨
    // 만약 dto의 변경이 일어나면 dto, controller, service까지만 고치면 됨
    // 만약 DTO가 Persistence layer까지 간다면 코드 변경에 repository의 변경까지 이어짐
    @Transactional(readOnly = true)
    public CommonResponseDto<?> showAllBoardList(Pageable pageable){
        // 커스텀 정렬 페이지
        // 해당 페이지는 list를 3개를 받아오며 id는 오름차순으로 정렬합니다.
        Page<Board> boardPage = boardRepository.showBoardPage(pageable);

        List<BoardResponseDTO> responseList =
                boardPage.map(board ->
                        BoardResponseDTO.builder()
                                .title(board.getTitle())
                                .description(board.getDescription())
                                .boardId(board.getId())
                                .memberId(board.getMember().getId())
                                .writer(board.getMember().getUsername())
                                .createAt(board.getCreatedAt())
                                .build()
                ).getContent();

        return new CommonResponseDto<>("모든 게시글 조회", 200, responseList);
    }

    @Transactional(readOnly = true)
    public CommonResponseDto<?> showMemberBoard(long memberId){
        Member member = memberRepository.findById(memberId).orElse(null);
        if(member == null){
            return new CommonResponseDto<>("해당 멤버는 존재하지 않습니다.", 400, null);
        }
        List<Board> memberBoard = boardRepository.findAllByMemberId(memberId);

        if(memberBoard == null){
            // 조회는 성공 했기 때문에 statusCode를 400으로 돌려줄 순 없음.
            return new CommonResponseDto<>("해당 멤버의 게시글이 존재하지 않습니다.", 200, null);
        }else{
            // member가 존재하면
            // 해당 사항은 title, description, createAt만 보여 줘도 됨
            List<BoardResponseDTO> responseDTO = new ArrayList<>();
            for(int i = 0; i<memberBoard.size(); i++){
                responseDTO.add(
                    BoardResponseDTO.builder()
                            .title(memberBoard.get(i).getTitle())
                            .description(memberBoard.get(i).getDescription())
                            .memberId(memberBoard.get(i).getMember().getId())       // memberId 대체 가능
                            .writer(memberBoard.get(i).getMember().getUsername())
                            .boardId(memberBoard.get(i).getId())
                            .createAt(memberBoard.get(i).getCreatedAt())
                            .build()
                );
            }
            return new CommonResponseDto<>("해당 멤버의 모든 게시글 조회 성공", 200, responseDTO);
        }
    }

    @Transactional(readOnly = true)
    public CommonResponseDto<BoardResponseDTO> showBoard(long boardId) {
        Board board = boardRepository.findById(boardId).orElse(null);
        if(board == null){
            return new CommonResponseDto<>("해당 게시글은 존재하지 않습니다.", 400, null);
        }
        BoardResponseDTO responseDTO = board.showBoard(board);

        return new CommonResponseDto<>("해당 게시글 조회 성공", 200, responseDTO);
    }

    @Transactional
    public CommonResponseDto<?> updateBoard(long boardId, BoardRequestDTO boardRequestDTO, MemberDetailsImpl memberDetails){
        Board board = boardRepository.findById(boardId).orElse(null);
        if(board == null){
            return new CommonResponseDto<>("해당 Board의 Id는 존재하지 않습니다.", 400, null);
        }
        Member member = memberRepository.findById(memberDetails.getMember().getId()).orElse(null);
        if(member == null){
            return new CommonResponseDto<>("해당 Member의 Id는 존재하지 않습니다.",400, null);
        }
        board.update(boardRequestDTO);
        boardRepository.save(board);
        return new CommonResponseDto<>("해당 게시글 수정 성공", 200, null);
    }
}
