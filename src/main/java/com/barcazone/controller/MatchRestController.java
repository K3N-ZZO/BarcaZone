package com.barcazone.controller;

import com.barcazone.dto.EventDto;
import com.barcazone.entity.Event;
import com.barcazone.repository.EventRepository;
import com.barcazone.service.MatchApiService;
import com.barcazone.api.ApiMapper; // Twój mapper do DTO
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/matches")
@RequiredArgsConstructor
public class MatchRestController {

    private final MatchApiService matchApiService;
    private final EventRepository events;

    @GetMapping
    public List<EventDto> list(@RequestParam(defaultValue = "recent") String type){
        List<Event> src = "upcoming".equalsIgnoreCase(type)
                ? matchApiService.fetchUpcomingMatches()
                : matchApiService.fetchRecentMatches();

        return src.stream().map(ApiMapper::toDto).toList();
    }

    @GetMapping("/{id}")
    public EventDto one(@PathVariable Long id){
        Event e = events.findById(id).orElseThrow();
        return ApiMapper.toDto(e);
    }
}
