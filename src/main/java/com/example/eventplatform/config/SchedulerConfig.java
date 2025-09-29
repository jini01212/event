package com.example.eventplatform.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.beans.factory.annotation.Autowired;

import com.example.eventplatform.service.ReservationService;
import com.example.eventplatform.entity.PaymentStatus;
import com.example.eventplatform.entity.Reservation;

import java.time.LocalDateTime;
import java.util.List;

@Configuration
@EnableScheduling
public class SchedulerConfig {

    @Autowired
    private ReservationService reservationService;

    /**
     * 매일 자정에 오래된 대기중인 예매 정리
     */
    @Scheduled(cron = "0 0 0 * * *")
    public void cleanupOldPendingReservations() {
        LocalDateTime cutoffDate = LocalDateTime.now().minusDays(1);
        List<Reservation> oldPendingReservations = reservationService
                .getReservationsByPeriod(LocalDateTime.now().minusYears(1), cutoffDate);

        // 1일 이상 된 결제 대기 예매들을 실패로 처리
        for (Reservation reservation : oldPendingReservations) {
            if (reservation.getPaymentStatus() == PaymentStatus.PENDING) {
                // paymentService.failPayment(reservation.getId(), "결제 시간 초과");
                System.out.println("정리 대상 예매: " + reservation.getTicketNumber());
            }
        }
    }
}