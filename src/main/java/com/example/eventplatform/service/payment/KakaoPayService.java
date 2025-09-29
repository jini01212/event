package com.example.eventplatform.service.payment;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

@Service
public class KakaoPayService {

    @Value("${payment.kakao.admin-key}")
    private String adminKey;

    @Value("${payment.kakao.cid}")
    private String cid;

    private final RestTemplate restTemplate = new RestTemplate();
    private static final String KAKAO_API_URL = "https://kapi.kakao.com/v1/payment";

    /**
     * 카카오페이 결제 준비
     */
    public Map<String, Object> ready(String orderId, String itemName, int totalAmount, String userEmail) {
        String url = KAKAO_API_URL + "/ready";

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        headers.set("Authorization", "KakaoAK " + adminKey);

        Map<String, Object> params = new HashMap<>();
        params.put("cid", cid);
        params.put("partner_order_id", orderId);
        params.put("partner_user_id", userEmail);
        params.put("item_name", itemName);
        params.put("quantity", 1);
        params.put("total_amount", totalAmount);
        params.put("tax_free_amount", 0);
        params.put("approval_url", "http://localhost:8080/payment/kakao/success");
        params.put("cancel_url", "http://localhost:8080/payment/kakao/cancel");
        params.put("fail_url", "http://localhost:8080/payment/kakao/fail");

        try {
            ResponseEntity<Map> response = restTemplate.postForEntity(url, params, Map.class, headers);
            return response.getBody();
        } catch (Exception e) {
            throw new RuntimeException("카카오페이 결제 준비 실패: " + e.getMessage());
        }
    }

    /**
     * 카카오페이 결제 승인
     */
    public Map<String, Object> approve(String tid, String orderId, String userEmail, String pgToken) {
        String url = KAKAO_API_URL + "/approve";

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        headers.set("Authorization", "KakaoAK " + adminKey);

        Map<String, Object> params = new HashMap<>();
        params.put("cid", cid);
        params.put("tid", tid);
        params.put("partner_order_id", orderId);
        params.put("partner_user_id", userEmail);
        params.put("pg_token", pgToken);

        try {
            ResponseEntity<Map> response = restTemplate.postForEntity(url, params, Map.class, headers);
            return response.getBody();
        } catch (Exception e) {
            throw new RuntimeException("카카오페이 결제 승인 실패: " + e.getMessage());
        }
    }
}
