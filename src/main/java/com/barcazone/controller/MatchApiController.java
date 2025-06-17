package com.barcazone.controller;

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

import java.util.List;

@Controller
@RequiredArgsConstructor
public class MatchApiController {

    private final MatchApiService matchApiService;
    private final EventRepository eventRepository;   // wstrzyknij repo
    private final CommentService commentService;

    @GetMapping("/")
    public String showBarcaResults(Model model) {
        List<Event> results = matchApiService.syncRecentMatches();
        model.addAttribute("matches", results);
        return "index";
    }

    @GetMapping("/matchDetails/{id}")
    public String showMatchDetails(@PathVariable Long id, Model model) {

        Event match = eventRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Brak meczu"));
        model.addAttribute("match", match);
        model.addAttribute("comments", match.getComments());

        return "match-details";
    }
    @PostMapping("/match/{id}/comment")
    public String addComment(@PathVariable Long id,
                             @RequestParam String author,
                             @RequestParam String content) {
        commentService.addComment(id, author, content);
        return "redirect:/matchDetails/" + id;
    }
}
