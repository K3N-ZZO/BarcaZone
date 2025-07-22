package com.barcazone.api;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
public class ApiEventResponse {
        @JsonProperty("events")
        @JsonAlias("results")
        private List<ApiEventDto> events;
}

