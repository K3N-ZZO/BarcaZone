package com.barcazone.service;

import com.barcazone.api.ApiEventDto;
import com.barcazone.api.ApiEventResponse;
import com.barcazone.entity.Event;
import com.barcazone.repository.EventRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


@Service
@RequiredArgsConstructor
public class MatchApiService {

    private final RestTemplate restTemplate;
    private final EventRepository eventRepository;

    @Transactional
    public List<Event> syncRecentMatches() {

        ApiEventResponse resp = restTemplate
                .getForObject("https://www.thesportsdb.com/api/v1/json/3/eventslast.php?id=133739",
                        ApiEventResponse.class);

        if (resp == null || resp.getEvents() == null) {
            return Collections.emptyList();
        }

        List<Event> result = new ArrayList<>();

        for (ApiEventDto dto : resp.getEvents()) {

            Event e = eventRepository.findByEventId(dto.getIdEvent())
                    .orElseGet(Event::new);

            e.setEventId(dto.getIdEvent());
            e.setDateEvent(dto.getDateEvent());
            e.setStrHomeTeam(dto.getStrHomeTeam());
            e.setStrAwayTeam(dto.getStrAwayTeam());
            e.setIntHomeScore(dto.getIntHomeScore());
            e.setIntAwayScore(dto.getIntAwayScore());

            result.add(eventRepository.save(e));
        }
        return result;
    }

    @Transactional
    public List<Event> syncUpcomingMatches() {

        ApiEventResponse resp = restTemplate.getForObject(
                "https://www.thesportsdb.com/api/v1/json/3/eventsnext.php?id=133739",
                ApiEventResponse.class
        );

        if (resp == null || resp.getEvents() == null) {
            return Collections.emptyList();
        }
        List<Event> result = new ArrayList<>();
        for (ApiEventDto dto : resp.getEvents()) {

            Event e = eventRepository.findByEventId(dto.getIdEvent()).orElseGet(Event::new);

            e.setEventId(dto.getIdEvent());
            e.setDateEvent(dto.getDateEvent());
            e.setStrHomeTeam(dto.getStrHomeTeam());
            e.setStrAwayTeam(dto.getStrAwayTeam());
            e.setIntHomeScore(dto.getIntHomeScore());
            e.setIntAwayScore(dto.getIntAwayScore());

            result.add(eventRepository.save(e));
        }
        return result;
    }
}
