package com.barcazone.api;

import com.barcazone.dto.CommentDto;
import com.barcazone.dto.CommentWithVotesDto;
import com.barcazone.dto.EventDto;
import com.barcazone.entity.Comment;
import com.barcazone.entity.CommentWithVotes;
import com.barcazone.entity.Event;

public final class ApiMapper {

    private ApiMapper() {}

    // Event -> EventDto
    public static EventDto toDto(Event e) {
        if (e == null) return null;
        EventDto d = new EventDto();
        d.setId(e.getId());
        d.setEventId(e.getEventId());
        d.setDateEvent(String.valueOf(e.getDateEvent())); // już String
        d.setStrHomeTeam(e.getStrHomeTeam());
        d.setStrAwayTeam(e.getStrAwayTeam());
        d.setIntHomeScore(e.getIntHomeScore());
        d.setIntAwayScore(e.getIntAwayScore());
        return d;
    }

    // Comment -> CommentDto
    public static CommentDto toDto(Comment c) {
        if (c == null) return null;
        CommentDto d = new CommentDto();
        d.setId(c.getId());
        d.setEventId(c.getEvent() != null ? c.getEvent().getId() : null);
        d.setAuthor(c.getAuthor());
        d.setContent(c.getContent());
        return d;
    }

    // CommentWithVotes -> CommentWithVotesDto
    public static CommentWithVotesDto toDto(CommentWithVotes cv) {
        if (cv == null) return null;
        CommentWithVotesDto out = new CommentWithVotesDto();
        out.setComment(toDto(cv.getComment()));
        out.setNet(cv.getNet());
        out.setSelfVote(cv.getSelfVote());
        return out;
    }
}
