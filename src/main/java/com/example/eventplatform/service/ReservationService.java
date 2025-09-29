package com.example.eventplatform.service;

import com.example.eventplatform.entity.PaymentStatus;
import com.example.eventplatform.entity.Reservation;
import com.example.eventplatform.repository.ReservationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ReservationService {

    @Autowired
    private ReservationRepository reservationRepository;

    /**
     * 예매 조회
     */
    public Reservation getReservation(Long id) {
        return reservationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("예매를 찾을 수 없습니다."));
    }

    /**
     * 티켓번호로 예매 조회
     */
    public Reservation getReservationByTicketNumber(String ticketNumber) {
        return reservationRepository.findByTicketNumber(ticketNumber)
                .orElseThrow(() -> new RuntimeException("유효하지 않은 티켓번호입니다."));
    }

    /**
     * 이메일로 예매 목록 조회
     */
    public List<Reservation> getReservationsByEmail(String email) {
        return reservationRepository.findByCustomerEmailOrderByCreatedAtDesc(email);
    }

    /**
     * 결제 대기중인 예매 목록
     */
    public List<Reservation> getPendingReservations() {
        return reservationRepository.findByPaymentStatusOrderByCreatedAtDesc(PaymentStatus.PENDING);
    }

    /**
     * 모든 예매 목록 (관리자용)
     */
    public List<Reservation> getAllReservations() {
        return reservationRepository.findAll();
    }

    /**
     * 이벤트별 예매 목록
     */
    public List<Reservation> getReservationsByEvent(Long eventId) {
        return reservationRepository.findByEventIdOrderByCreatedAtDesc(eventId);
    }
}