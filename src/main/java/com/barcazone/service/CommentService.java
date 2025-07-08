package com.barcazone.service;

import com.barcazone.entity.*;


import com.barcazone.repository.CommentRepository;
import com.barcazone.repository.EventRepository;
import com.barcazone.repository.UserRepository;
import com.barcazone.repository.VoteRepository;
import org.springframework.stereotype.Service;


import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


@Service
public class CommentService {
    private final CommentRepository commentRepository;
    private final EventRepository eventRepository;
    private final UserRepository userRepository;
    private final VoteRepository voteRepository;

    public CommentService(CommentRepository commentRepository, EventRepository eventRepository, VoteRepository voteRepository, UserRepository userRepository) {
        this.commentRepository = commentRepository;
        this.eventRepository = eventRepository;
        this.userRepository = userRepository;
        this.voteRepository = voteRepository;
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

    public void vote(Long commentId, String username, int value) {
        User user = userRepository.findByUsername(username).orElseThrow(() -> new RuntimeException(
                "Nie znaleziono użytkownika"));
        Comment comment = commentRepository.findById(commentId).orElseThrow(() -> new RuntimeException(
                "Nie znaleziono komentarza"));
        Vote vote = voteRepository.findByUserAndComment(user, comment)
                .orElse(new Vote(null, user, comment,0));
        vote.setValue(value);
        voteRepository.save(vote);
    }

    public List<CommentWithVotes> getCommentWithVotes(Long matchId, String username) {
        List<Comment> all = commentRepository.findByEvent_Id(matchId);
        return all.stream()
                .map(c -> {
                    List<Vote> votes = voteRepository.findByComment(c);
                    int net = votes.stream().mapToInt(Vote::getValue).sum();
                    Optional<Vote> self = (username ==null)
                            ? Optional.empty()
                            : voteRepository.findByUserAndComment((userRepository.findByUsername(username).orElse(null)),c);
                            return new CommentWithVotes(c,net,self.map(Vote::getValue).orElse(0));
                })
                .sorted(Comparator.comparingInt(CommentWithVotes::getNet).reversed())
                .collect(Collectors.toList());
    }


}
