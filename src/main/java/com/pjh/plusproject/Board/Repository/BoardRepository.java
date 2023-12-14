package com.pjh.plusproject.Board.Repository;

import com.pjh.plusproject.Board.Entity.Board;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface BoardRepository extends JpaRepository<Board, Long> {
    @Query(value = "SELECT * FROM board ORDER BY board.id DESC", nativeQuery = true)
    Page<Board> showBoardPage(Pageable pageable);

    Optional<Board> findByMemberId(long memberId);

    @Query(value = "SELECT * FROM board WHERE board.member_id = :memberId ORDER BY board.id DESC", nativeQuery = true)
    List<Board> findAllByMemberId(long memberId);
}
