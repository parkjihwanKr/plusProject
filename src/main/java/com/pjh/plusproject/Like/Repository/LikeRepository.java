package com.pjh.plusproject.Like.Repository;

import com.pjh.plusproject.Like.Entity.Like;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface LikeRepository extends JpaRepository<Like, Long> {

    @Query(value = "DELETE FROM likes WHERE board_id = :boardId ",nativeQuery = true)
    void deleteByBoardId(long boardId);
    @Query(value = "DELETE FROM likes WHERE to_member_id = :memberId ",nativeQuery = true)
    void deleteByMemberId(long memberId);

}
