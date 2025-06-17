package com.barcazone.service;

import com.barcazone.entity.Event;


import com.barcazone.entity.Comment;
import com.barcazone.repository.CommentRepository;
import com.barcazone.repository.EventRepository;
import org.springframework.stereotype.Service;


import java.util.List;


@Service
public class CommentService {
    private final CommentRepository commentRepository;
    private final EventRepository eventRepository;

    public CommentService(CommentRepository commentRepository, EventRepository eventRepository) {
        this.commentRepository = commentRepository;
        this.eventRepository = eventRepository;
    }

    public List<Comment> getComments(Long matchId) {
        return commentRepository.findByEvent_Id(matchId);
    }

    public void addComment(Long matchId, String author, String content) {
        Event event = eventRepository.findById(matchId)
                .orElseThrow(() -> new RuntimeException("Brak meczu"));
        Comment c = new Comment();
        c.setEvent(event);
        c.setAuthor(author);
        c.setContent(content);
        commentRepository.save(c);
    }
}
