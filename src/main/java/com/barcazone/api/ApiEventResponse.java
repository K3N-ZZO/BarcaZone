package com.barcazone.api;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.Getter;

import java.util.List;

@Getter
@Data
public class ApiEventResponse {

    @JsonProperty("results")
    private List<ApiEventDto> events;
}
