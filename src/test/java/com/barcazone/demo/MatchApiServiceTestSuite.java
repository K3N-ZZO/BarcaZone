package com.barcazone.demo;

import com.barcazone.api.ApiEventDto;
import com.barcazone.api.ApiEventResponse;
import com.barcazone.entity.Event;
import com.barcazone.repository.EventRepository;
import com.barcazone.service.MatchApiService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class MatchApiServiceTestSuite {
    @Mock
    private RestTemplate restTemplate;

    @Mock
    private EventRepository eventRepository;

    @InjectMocks
    private MatchApiService service;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void syncRecentMatches_emptyResponse() {
        when(restTemplate.getForObject(anyString(), eq(ApiEventResponse.class)))
                .thenReturn(null);

        List<Event> result = service.syncRecentMatches();
        assertTrue(result.isEmpty());
    }

    @Test
    void syncRecentMatches_savesNewEvent() {
        ApiEventDto dto = new ApiEventDto();
        dto.setIdEvent("E1");
        dto.setDateEvent("2025-06-01");
        dto.setStrHomeTeam("Barca");
        dto.setStrAwayTeam("Real");
        dto.setIntHomeScore("2");
        dto.setIntAwayScore("1");
        ApiEventResponse resp = new ApiEventResponse();
        resp.setEvents(List.of(dto));

        when(restTemplate.getForObject(anyString(), eq(ApiEventResponse.class)))
                .thenReturn(resp);
        when(eventRepository.findByEventId("E1")).thenReturn(Optional.empty());
        when(eventRepository.save(any(Event.class))).thenAnswer(inv -> inv.getArgument(0));

        List<Event> result = service.syncRecentMatches();
        assertEquals(1, result.size());
        Event saved = result.get(0);
        assertEquals("E1", saved.getEventId());
        verify(eventRepository).save(any(Event.class));
    }
}
