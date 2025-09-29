package com.example.eventplatform.service;

import com.example.eventplatform.entity.PaymentMethod;
import com.example.eventplatform.entity.Reservation;
import com.example.eventplatform.service.payment.PaymentStrategy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class EnhancedPaymentService {

    @Autowired
    private List<PaymentStrategy> paymentStrategies;

    /**
     * 결제 방식에 따른 전략 선택
     */
    public Map<String, Object> processPayment(Reservation reservation) {
        PaymentMethod method = reservation.getPaymentMethod();

        Optional<PaymentStrategy> strategy = paymentStrategies.stream()
                .filter(s -> s.getSupportedMethod() == method)
                .findFirst();

        if (strategy.isPresent()) {
            return strategy.get().initiatePayment(reservation);
        } else {
            throw new RuntimeException("지원하지 않는 결제 방식입니다: " + method);
        }
    }

    /**
     * 웹훅 처리
     */
    public void handleWebhook(PaymentMethod method, Map<String, Object> payload) {
        Optional<PaymentStrategy> strategy = paymentStrategies.stream()
                .filter(s -> s.getSupportedMethod() == method)
                .findFirst();

        if (strategy.isPresent()) {
            strategy.get().processWebhook(payload);
        } else {
            throw new RuntimeException("지원하지 않는 결제 방식입니다: " + method);
        }
    }
}
