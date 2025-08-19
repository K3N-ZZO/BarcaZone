package com.barcazone.dto;

import lombok.Data;

@Data
public class EventDto {
    private Long id;
    private String eventId;
    private String dateEvent;
    private String strHomeTeam;
    private String strAwayTeam;
    private String intHomeScore;
    private String intAwayScore;
}

