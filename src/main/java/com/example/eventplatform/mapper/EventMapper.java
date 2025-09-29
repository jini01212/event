package com.example.eventplatform.mapper;

import com.example.eventplatform.dto.EventDto;
import com.example.eventplatform.entity.Event;
import org.springframework.stereotype.Component;

@Component
public class EventMapper {

    public EventDto toDto(Event event) {
        if (event == null) return null;

        return new EventDto(
                event.getId(),
                event.getTitle(),
                event.getDescription(),
                event.getLocation(),
                event.getEventDate(),
                event.getPrice(),
                event.getMaxTickets(),
                event.getSoldTickets(),
                event.getImageUrl(),
                event.getActive()
        );
    }

    public Event toEntity(EventDto dto) {
        if (dto == null) return null;

        Event event = new Event();
        event.setId(dto.getId());
        event.setTitle(dto.getTitle());
        event.setDescription(dto.getDescription());
        event.setLocation(dto.getLocation());
        event.setEventDate(dto.getEventDate());
        event.setPrice(dto.getPrice());
        event.setMaxTickets(dto.getMaxTickets());
        event.setSoldTickets(dto.getSoldTickets());
        event.setImageUrl(dto.getImageUrl());
        event.setActive(dto.getActive());

        return event;
    }
}
