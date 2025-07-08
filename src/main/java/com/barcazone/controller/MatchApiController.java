package com.barcazone.controller;

import com.barcazone.entity.CommentWithVotes;
import com.barcazone.entity.Event;

import com.barcazone.repository.EventRepository;
import com.barcazone.service.CommentService;
import com.barcazone.service.MatchApiService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.security.Principal;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class MatchApiController {

    private final MatchApiService matchApiService;
    private final EventRepository eventRepository;
    private final CommentService commentService;

    @GetMapping("/")
    public String showBarcaResults(Model model) {
        List<Event> recent = matchApiService.syncRecentMatches();
        List<Event> upcoming = matchApiService.syncUpcomingMatches();
        model.addAttribute("matches", recent);
        model.addAttribute("upcoming", upcoming);
        return "index";
    }

    @GetMapping("/matchDetails/{id}")
    public String showMatchDetails(@PathVariable Long id,
                                   Model model,
                                   Principal principal) {
        Event match = eventRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Brak meczu"));
        model.addAttribute("match", match);

        String username = principal != null ? principal.getName() : null;
        List<CommentWithVotes> commentsWithVotes =
                commentService.getCommentWithVotes(id, username);
        model.addAttribute("commentsWithVotes", commentsWithVotes);

        return "match-details";
    }

    @PostMapping("/comment/{commentId}/vote")
    public String voteComment(@PathVariable Long commentId,
                              @RequestParam String type,
                              @RequestParam Long matchId,
                              Principal principal) {

        int value = "up".equals(type) ? 1 : -1;
        commentService.vote(commentId, principal.getName(), value);
        // po zapisaniu głosu przekierowujemy z powrotem, żeby odświeżyć liczniki
        return "redirect:/matchDetails/" + matchId;
    }

    @PostMapping("/match/{id}/comment")
    public String addComment(@PathVariable Long id,
                             @RequestParam String content,
                             Principal principal) {
        commentService.addComment(id, principal.getName(), content);
        return "redirect:/matchDetails/" + id;
    }
}
