package com.pjh.plusproject.Board.Service;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.pjh.plusproject.Board.DTO.BoardRequestDTO;
import com.pjh.plusproject.Board.DTO.BoardResponseDTO;
import com.pjh.plusproject.Board.Entity.Board;
import com.pjh.plusproject.Board.Repository.BoardRepository;
import com.pjh.plusproject.Global.Common.CommonResponseDto;
import com.pjh.plusproject.Global.Security.MemberDetailsImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

@Slf4j
@Service
public class BoardService {
    private final BoardRepository boardRepository;
    private final AmazonS3 amazonS3;

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    public BoardService(BoardRepository boardRepository, AmazonS3 amazonS3){
        this.boardRepository = boardRepository;
        this.amazonS3 = amazonS3;
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
    public CommonResponseDto<?> getAllBoardList(Pageable pageable){
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
    @Transactional
    public CommonResponseDto<?> createBoard(
            MultipartFile image,
            BoardRequestDTO requestDTO,
            MemberDetailsImpl memberDetails) throws IOException {
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
}
