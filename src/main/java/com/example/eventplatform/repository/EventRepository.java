package com.example.eventplatform.repository;

import com.example.eventplatform.entity.Event;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface EventRepository extends JpaRepository<Event, Long> {
    List<Event> findByActiveTrue();
    List<Event> findByActiveTrueAndEventDateAfter(LocalDateTime date);
    List<Event> findByActiveTrueOrderByEventDateAsc();
}
