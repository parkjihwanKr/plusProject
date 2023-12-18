package com.pjh.plusproject.Like.Repository;

import com.pjh.plusproject.Like.Entity.Like;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface LikeRepository extends JpaRepository<Like, Long> {

    void deleteByBoardId(long boardId);
    void deleteByToMemberId(long memberId);
    boolean existsByBoardIdAndFromMemberId(long boardId, Long fromMemberId);

    boolean existsByBoardId(long boardId);

    boolean existsByToMemberIdAndFromMemberId(Long fromMemberId, Long toMemberId);
}
