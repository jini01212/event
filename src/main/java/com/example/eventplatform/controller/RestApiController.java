package com.example.eventplatform.controller;

import com.example.eventplatform.entity.Event;
import com.example.eventplatform.entity.Reservation;
import com.example.eventplatform.service.EventService;
import com.example.eventplatform.service.ReservationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.HashMap;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "*") // 개발용, 운영시에는 특정 도메인으로 제한
public class RestApiController {

    @Autowired
    private EventService eventService;

    @Autowired
    private ReservationService reservationService;

    /**
     * 활성 이벤트 목록 API
     */
    @GetMapping("/events")
    public ResponseEntity<List<Event>> getActiveEvents() {
        List<Event> events = eventService.getActiveEvents();
        return ResponseEntity.ok(events);
    }

    /**
     * 이벤트 상세 API
     */
    @GetMapping("/events/{id}")
    public ResponseEntity<Event> getEvent(@PathVariable Long id) {
        try {
            Event event = eventService.getEvent(id);
            return ResponseEntity.ok(event);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * 티켓 조회 API
     */
    @GetMapping("/tickets/{ticketNumber}")
    public ResponseEntity<Reservation> getTicket(@PathVariable String ticketNumber) {
        try {
            Reservation reservation = reservationService.getReservationByTicketNumber(ticketNumber);
            return ResponseEntity.ok(reservation);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * 시스템 상태 확인 API
     */
    @GetMapping("/health")
    public ResponseEntity<Map<String, Object>> healthCheck() {
        Map<String, Object> status = new HashMap<>();
        status.put("status", "OK");
        status.put("timestamp", System.currentTimeMillis());
        status.put("version", "1.0.0");

        return ResponseEntity.ok(status);
    }
}
