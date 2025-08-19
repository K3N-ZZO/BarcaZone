package com.barcazone.dto;

import lombok.Data;

@Data
public class CommentWithVotesDto {
    private CommentDto comment;
    private int net;
    private int selfVote;
}
