package com.barcazone.repository;

import com.barcazone.entity.Comment;
import com.barcazone.entity.User;
import com.barcazone.entity.Vote;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface VoteRepository extends JpaRepository<Vote, Long> {
    Optional<Vote> findByUserAndComment(User user, Comment comment);
    List<Vote> findByComment(Comment comment);
}
