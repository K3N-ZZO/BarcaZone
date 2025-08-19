package com.barcazone.service;

import com.barcazone.api.ApiEventDto;
import com.barcazone.api.ApiEventResponse;
import com.barcazone.entity.Event;
import com.barcazone.repository.EventRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
@RequiredArgsConstructor
public class MatchApiService {

    private final RestTemplate restTemplate;
    private final EventRepository eventRepository;

    /** ZWRACA ostatnie mecze i zapisuje je w DB */
    public List<Event> fetchRecentMatches() {
        ApiEventResponse resp = restTemplate.getForObject(
                "https://www.thesportsdb.com/api/v1/json/3/eventslast.php?id=133739",
                ApiEventResponse.class
        );
        return saveAll(resp);
    }

    /** ZWRACA nadchodzące mecze i zapisuje je w DB */
    public List<Event> fetchUpcomingMatches() {
        ApiEventResponse resp = restTemplate.getForObject(
                "https://www.thesportsdb.com/api/v1/json/123/eventsnext.php?id=133739",
                ApiEventResponse.class
        );
        return saveAll(resp);
    }

    /** Zadanie CRON – nic nie zwraca; tylko wywołuje powyższe */
    @Scheduled(cron = "0 0 * * * *") // co godzinę; ustaw jak chcesz
    public void runSyncJob() {
        fetchRecentMatches();
        fetchUpcomingMatches();
    }

    private List<Event> saveAll(ApiEventResponse resp) {
        if (resp == null || resp.getEvents() == null) return Collections.emptyList();
        List<Event> out = new ArrayList<>();
        for (ApiEventDto dto : resp.getEvents()) {
            Event e = eventRepository.findByEventId(dto.getIdEvent()).orElseGet(Event::new);
            e.setEventId(dto.getIdEvent());
            e.setDateEvent(LocalDate.parse(dto.getDateEvent()));
            e.setStrHomeTeam(dto.getStrHomeTeam());
            e.setStrAwayTeam(dto.getStrAwayTeam());
            e.setIntHomeScore(dto.getIntHomeScore());
            e.setIntAwayScore(dto.getIntAwayScore());
            out.add(eventRepository.save(e));
        }
        return out;
    }
}
