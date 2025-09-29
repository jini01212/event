package com.example.eventplatform.service.payment;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

@Service
public class TossPaymentService {

    @Value("${payment.toss.client-key}")
    private String clientKey;

    @Value("${payment.toss.secret-key}")
    private String secretKey;

    private final RestTemplate restTemplate = new RestTemplate();
    private static final String TOSS_API_URL = "https://api.tosspayments.com/v1/payments";

    /**
     * 토스페이먼츠 결제 요청
     */
    public Map<String, Object> requestPayment(String orderId, int amount, String orderName, String customerName) {
        Map<String, Object> paymentRequest = new HashMap<>();
        paymentRequest.put("amount", amount);
        paymentRequest.put("orderId", orderId);
        paymentRequest.put("orderName", orderName);
        paymentRequest.put("customerName", customerName);
        paymentRequest.put("successUrl", "http://localhost:8080/payment/toss/success");
        paymentRequest.put("failUrl", "http://localhost:8080/payment/toss/fail");

        return paymentRequest;
    }

    /**
     * 결제 승인 (웹훅에서 호출)
     */
    public Map<String, Object> confirmPayment(String paymentKey, String orderId, int amount) {
        String url = TOSS_API_URL + "/confirm";

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBasicAuth(secretKey, "");

        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("paymentKey", paymentKey);
        requestBody.put("orderId", orderId);
        requestBody.put("amount", amount);

        HttpEntity<Map<String, Object>> request = new HttpEntity<>(requestBody, headers);

        try {
            ResponseEntity<Map> response = restTemplate.postForEntity(url, request, Map.class);
            return response.getBody();
        } catch (Exception e) {
            throw new RuntimeException("토스페이먼츠 결제 승인 실패: " + e.getMessage());
        }
    }

    /**
     * 결제 취소
     */
    public Map<String, Object> cancelPayment(String paymentKey, String cancelReason) {
        String url = TOSS_API_URL + "/" + paymentKey + "/cancel";

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBasicAuth(secretKey, "");

        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("cancelReason", cancelReason);

        HttpEntity<Map<String, Object>> request = new HttpEntity<>(requestBody, headers);

        try {
            ResponseEntity<Map> response = restTemplate.postForEntity(url, request, Map.class);
            return response.getBody();
        } catch (Exception e) {
            throw new RuntimeException("결제 취소 실패: " + e.getMessage());
        }
    }
}
