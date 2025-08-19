package com.barcazone.dto;

import lombok.Data;

@Data
public class CommentDto {
    private Long id;
    private Long eventId;
    private String author;
    private String content;
}
