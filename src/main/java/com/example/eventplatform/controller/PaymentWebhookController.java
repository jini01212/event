package com.example.eventplatform.controller;

import com.example.eventplatform.service.PaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/webhook")
public class PaymentWebhookController {

    @Autowired
    private PaymentService paymentService;

    /**
     * 토스페이먼츠 Webhook
     */
    @PostMapping("/toss")
    public ResponseEntity<String> handleTossWebhook(@RequestBody Map<String, Object> payload) {
        try {
            String orderId = (String) payload.get("orderId");
            String status = (String) payload.get("status");
            String paymentKey = (String) payload.get("paymentKey");

            if ("DONE".equals(status)) {
                // 티켓번호로 예매 조회 후 결제 완료 처리
                paymentService.confirmPaymentByTicketNumber(orderId, paymentKey);
            }

            return ResponseEntity.ok("OK");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("ERROR");
        }
    }

    /**
     * 카카오페이 Webhook
     */
    @PostMapping("/kakao")
    public ResponseEntity<String> handleKakaoWebhook(@RequestBody Map<String, Object> payload) {
        try {
            String partnerOrderId = (String) payload.get("partner_order_id");
            String status = (String) payload.get("status");
            String tid = (String) payload.get("tid");

            if ("SUCCESS".equals(status)) {
                paymentService.confirmPaymentByTicketNumber(partnerOrderId, tid);
            }

            return ResponseEntity.ok("OK");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("ERROR");
        }
    }
}