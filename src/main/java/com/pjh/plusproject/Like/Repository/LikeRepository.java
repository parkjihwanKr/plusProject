package com.pjh.plusproject.Like.Repository;

import com.pjh.plusproject.Like.Entity.Like;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LikeRepository extends JpaRepository<Like, Long> {

    void deleteByBoardId(long boardId);
    void deleteByToMemberId(long memberId);
    boolean existsByBoardIdAndFromMemberId(long boardId, Long fromMemberId);
    boolean existsByToMemberIdAndFromMemberId(Long fromMemberId, Long toMemberId);
}
