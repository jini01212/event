package com.example.eventplatform.service;

import com.example.eventplatform.entity.Event;
import com.example.eventplatform.repository.EventRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EventServiceTest {

    @Mock
    private EventRepository eventRepository;

    @InjectMocks
    private EventService eventService;

    @Test
    void 활성_이벤트_목록_조회_성공() {
        // Given
        Event event1 = new Event("Test Event 1", "Description 1", "Location 1",
                LocalDateTime.now().plusDays(1), 10000, 100);
        Event event2 = new Event("Test Event 2", "Description 2", "Location 2",
                LocalDateTime.now().plusDays(2), 20000, 50);

        when(eventRepository.findByActiveTrueOrderByEventDateAsc())
                .thenReturn(Arrays.asList(event1, event2));

        // When
        List<Event> result = eventService.getActiveEvents();

        // Then
        assertEquals(2, result.size());
        assertEquals("Test Event 1", result.get(0).getTitle());
        verify(eventRepository).findByActiveTrueOrderByEventDateAsc();
    }

    @Test
    void 이벤트_조회_실패_예외발생() {
        // Given
        Long nonExistentId = 999L;
        when(eventRepository.findById(nonExistentId)).thenReturn(java.util.Optional.empty());

        // When & Then
        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> eventService.getEvent(nonExistentId));
        assertEquals("이벤트를 찾을 수 없습니다.", exception.getMessage());
    }
}
