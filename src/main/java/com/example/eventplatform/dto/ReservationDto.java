package com.example.eventplatform.dto;

import com.example.eventplatform.entity.PaymentMethod;
import com.example.eventplatform.entity.PaymentStatus;

import java.time.LocalDateTime;

public class ReservationDto {
    private Long id;
    private String ticketNumber;
    private String customerName;
    private String customerEmail;
    private String customerPhone;
    private Integer ticketCount;
    private Integer totalPrice;
    private PaymentStatus paymentStatus;
    private PaymentMethod paymentMethod;
    private LocalDateTime createdAt;
    private LocalDateTime paidAt;
    private Boolean used;
    private LocalDateTime usedAt;

    // Event 정보
    private String eventTitle;
    private LocalDateTime eventDate;
    private String eventLocation;

    // 생성자들
    public ReservationDto() {}

    // Getters and Setters (생략 - 위와 동일한 패턴)
    // ... (모든 필드에 대한 getter/setter 구현)
}