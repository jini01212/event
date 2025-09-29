package com.example.eventplatform.repository;

import com.example.eventplatform.entity.PaymentStatus;
import com.example.eventplatform.entity.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;


import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface ReservationRepository extends JpaRepository<Reservation, Long> {

    // 티켓번호로 조회
    Optional<Reservation> findByTicketNumber(String ticketNumber);

    // 이메일로 예매 목록 조회 (최신순)
    List<Reservation> findByCustomerEmailOrderByCreatedAtDesc(String customerEmail);

    // 결제 상태로 조회
    List<Reservation> findByPaymentStatus(PaymentStatus status);

    // 이벤트별 예매 목록 조회 (최신순)
    List<Reservation> findByEventIdOrderByCreatedAtDesc(Long eventId);

    // 결제 상태별 예매 목록 조회 (최신순)
    List<Reservation> findByPaymentStatusOrderByCreatedAtDesc(PaymentStatus status);

    // 오늘 예매 목록 조회
    @Query("SELECT r FROM Reservation r WHERE DATE(r.createdAt) = CURRENT_DATE")
    List<Reservation> findTodayReservations();

    // 총 매출 계산 (결제 완료된 것만)
    @Query("SELECT COALESCE(SUM(r.totalPrice), 0) FROM Reservation r WHERE r.paymentStatus = 'COMPLETED'")
    Long getTotalSales();

    // 특정 기간 예매 조회
    List<Reservation> findByCreatedAtBetween(LocalDateTime startDate, LocalDateTime endDate);

    // 이벤트와 결제 상태로 조회
    List<Reservation> findByEventIdAndPaymentStatus(Long eventId, PaymentStatus status);

    // 사용 여부로 조회
    List<Reservation> findByUsed(Boolean used);

    // 이벤트와 사용 여부로 조회
    List<Reservation> findByEventIdAndUsed(Long eventId, Boolean used);

    List<Reservation> findByCreatedAtBetweenOrderByCreatedAtDesc(LocalDateTime startDate, LocalDateTime endDate);
}