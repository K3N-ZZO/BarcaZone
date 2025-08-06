package com.barcazone.service;

import com.barcazone.api.ApiEventDto;
import com.barcazone.api.ApiEventResponse;
import com.barcazone.entity.Event;
import com.barcazone.repository.EventRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
public class MatchApiService {

    private final RestTemplate restTemplate;
    private final EventRepository eventRepository;

    @Scheduled(cron = "0 0 0 * * *")
    @Transactional
    public void syncRecentMatches() {

        ApiEventResponse resp = restTemplate
                .getForObject("https://www.thesportsdb.com/api/v1/json/3/eventslast.php?id=133739",
                        ApiEventResponse.class);

        saveEvents(resp);
    }

    @Scheduled(cron = "0 0 0 * * *")
    @Transactional
    public void syncUpcomingMatches() {

        ApiEventResponse resp = restTemplate.getForObject(
                "https://www.thesportsdb.com/api/v1/json/123/eventsnext.php?id=133739",
                ApiEventResponse.class
        );

        saveEvents(resp);
    }

    private void saveEvents(ApiEventResponse resp) {
        if (resp == null || resp.getEvents() == null) return;
        for (var dto : resp.getEvents()) {
            Event e = eventRepository.findByEventId(dto.getIdEvent())
                    .orElseGet(Event::new);
            e.setEventId(dto.getIdEvent());
            e.setDateEvent(LocalDate.parse(dto.getDateEvent()));
            e.setStrHomeTeam(dto.getStrHomeTeam());
            e.setStrAwayTeam(dto.getStrAwayTeam());
            e.setIntHomeScore(dto.getIntHomeScore());
            e.setIntAwayScore(dto.getIntAwayScore());
            e.setTimeEvent(dto.getStrTime());
            eventRepository.save(e);
        }
    }

    public List<Event> getRecentFromDb(int limit) {
        LocalDate today = LocalDate.now();
        return eventRepository.findAll().stream()
                .filter(e -> e.getDateEvent().isBefore(today))
                .sorted(Comparator.comparing(Event::getDateEvent).reversed())
                .limit(limit)
                .collect(Collectors.toList());
    }

    public List<Event> getUpcomingFromDb(int limit) {
        LocalDate today = LocalDate.now();
        return eventRepository.findAll().stream()
                .filter(e -> !e.getDateEvent().isBefore(today))
                .sorted(Comparator.comparing(Event::getDateEvent))
                .limit(limit)
                .collect(Collectors.toList());
    }
}
