package com.barcazone.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class CommentWithVotes {
    private final Comment comment;
    private final int net;
    private final int selfVote;
}
