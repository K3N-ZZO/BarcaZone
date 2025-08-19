package com.barcazone.controller;

import com.barcazone.api.ApiMapper;
import com.barcazone.dto.CommentWithVotesDto;
import com.barcazone.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class CommentRestController {
    private final CommentService commentService;

    @GetMapping("/matches/{id}/comments")
    public List<CommentWithVotesDto> list(@PathVariable Long id, @AuthenticationPrincipal UserDetails me){
        String username = me!=null ? me.getUsername() : null;
        return commentService.getCommentWithVotes(id, username).stream()
                .map(ApiMapper::toDto)
                .toList();
    }

    @PostMapping("/matches/{id}/comments")
    public void add(@PathVariable Long id, @RequestBody Map<String,String> body, @AuthenticationPrincipal UserDetails me){
        commentService.addComment(id, me.getUsername(), body.get("content"));
    }

    @PostMapping("/comments/{commentId}/vote")
    public void vote(@PathVariable Long commentId, @RequestBody Map<String,Integer> body, @AuthenticationPrincipal UserDetails me){
        int value = body.getOrDefault("value", 0);
        commentService.vote(commentId, me.getUsername(), value);
    }
}
