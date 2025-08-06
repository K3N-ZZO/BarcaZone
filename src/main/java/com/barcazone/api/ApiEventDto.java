package com.barcazone.api;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class ApiEventDto {
    @JsonProperty("idEvent")
    private String idEvent;

    @JsonProperty("dateEvent")
    private String dateEvent;

    @JsonProperty("strHomeTeam")
    private String strHomeTeam;

    @JsonProperty("strAwayTeam")
    private String strAwayTeam;

    @JsonProperty("intHomeScore")
    private String intHomeScore;

    @JsonProperty("intAwayScore")
    private String intAwayScore;

    @JsonProperty("strTime")
    private String strTime;
}
