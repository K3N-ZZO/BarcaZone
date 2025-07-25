package com.barcazone.repository;

import com.barcazone.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {

    List<Comment> findByEvent_Id(Long eventId);
}
