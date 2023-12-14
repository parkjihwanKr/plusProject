package com.pjh.plusproject.Board.Repository;

import com.pjh.plusproject.Board.Entity.Board;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface BoardRepository extends JpaRepository<Board, Long> {
    @Query(value = "SELECT * FROM board ORDER BY board.id DESC", nativeQuery = true)
    Page<Board> showBoardPage(Pageable pageable);
}
