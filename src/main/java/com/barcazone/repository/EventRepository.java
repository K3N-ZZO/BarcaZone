package com.barcazone.repository;

import com.barcazone.entity.Event;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface EventRepository extends JpaRepository<Event, Long> {
    Optional<Event> findByEventId(String eventId);

    List<Event> findTopNByOrderByDateEventDesc(int n);

    List<Event> findTopNByOrderByDateEventAsc(int n);
}
