package com.pjh.plusproject.Board.Repository;

import com.pjh.plusproject.Board.Entity.Board;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BoardRepository extends JpaRepository<Board, Long> {
}
