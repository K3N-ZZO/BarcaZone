package com.barcazone.repository;

import com.barcazone.entity.Event;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;


import java.util.List;
import java.util.Optional;

public interface EventRepository extends JpaRepository<Event, Long> {
    Optional<Event> findByEventId(String eventId);

    Page<Event> findAllByOrderByDateEventDesc(Pageable pageable);

    Page<Event> findAllByOrderByDateEventAsc(Pageable pageable);
}
