package com.example.eventplatform.dto;

import java.time.LocalDateTime;

public class EventDto {
    private Long id;
    private String title;
    private String description;
    private String location;
    private LocalDateTime eventDate;
    private Integer price;
    private Integer maxTickets;
    private Integer soldTickets;
    private String imageUrl;
    private Boolean active;

    // 생성자들
    public EventDto() {}

    public EventDto(Long id, String title, String description, String location,
                    LocalDateTime eventDate, Integer price, Integer maxTickets,
                    Integer soldTickets, String imageUrl, Boolean active) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.location = location;
        this.eventDate = eventDate;
        this.price = price;
        this.maxTickets = maxTickets;
        this.soldTickets = soldTickets;
        this.imageUrl = imageUrl;
        this.active = active;
    }

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getLocation() { return location; }
    public void setLocation(String location) { this.location = location; }

    public LocalDateTime getEventDate() { return eventDate; }
    public void setEventDate(LocalDateTime eventDate) { this.eventDate = eventDate; }

    public Integer getPrice() { return price; }
    public void setPrice(Integer price) { this.price = price; }

    public Integer getMaxTickets() { return maxTickets; }
    public void setMaxTickets(Integer maxTickets) { this.maxTickets = maxTickets; }

    public Integer getSoldTickets() { return soldTickets; }
    public void setSoldTickets(Integer soldTickets) { this.soldTickets = soldTickets; }

    public String getImageUrl() { return imageUrl; }
    public void setImageUrl(String imageUrl) { this.imageUrl = imageUrl; }

    public Boolean getActive() { return active; }
    public void setActive(Boolean active) { this.active = active; }

    public Integer getAvailableTickets() {
        return maxTickets - soldTickets;
    }
}