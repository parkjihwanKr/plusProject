package com.pjh.plusproject.Comment.Repository;

import com.pjh.plusproject.Comment.Entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CommentRepository extends JpaRepository<Comment, Long> {

}
