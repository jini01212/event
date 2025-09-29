package com.example.eventplatform.service.payment;

import com.example.eventplatform.entity.PaymentMethod;
import com.example.eventplatform.entity.Reservation;

import java.util.Map;

public interface PaymentStrategy {
    PaymentMethod getSupportedMethod();
    Map<String, Object> initiatePayment(Reservation reservation);
    void processWebhook(Map<String, Object> payload);
}
