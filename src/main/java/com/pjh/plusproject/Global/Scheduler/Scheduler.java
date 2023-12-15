package com.pjh.plusproject.Global.Scheduler;

import com.pjh.plusproject.Board.Entity.Board;
import com.pjh.plusproject.Board.Service.BoardService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Component
@Slf4j(topic = "Scheduler")
@EnableScheduling
@RequiredArgsConstructor
public class Scheduler {

    private final BoardService boardService;

    // 해당 스케줄러는 Business layer의 데이터 가공에만 초점을 맞춤
    // 스케줄러가 Presentation layer와 Persistence layer에 침범하지 않도록 주의
    @Scheduled(cron = "0 0 0 * * ?")
    public void performTest(){
        // 수정된지 90일이 지난 데이터는 자동으로 지우는 스케줄러 기능을 개발해보기.
        // 데이터 삭제 및 백업도 굉장히 중요한 기능인데, 수강생들이 이런 내용을 잘 인지하지 못 함.

        // 지금 현 날짜 00시의 -90일
        LocalDateTime ninetyDaysAgo = LocalDateTime.now().minusDays(90);

        List<Board> boardList = boardService.findBoardCreatedBefore90Days(ninetyDaysAgo);

        if(boardList != null){
            for(int i = 0; i<boardList.size(); i++){
                boardService.deleteBoard(boardList.get(i).getId());
            }
        }
    }
}