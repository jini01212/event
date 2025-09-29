package com.example.eventplatform.service;

import com.example.eventplatform.entity.*;
import com.example.eventplatform.repository.ReservationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Service
@Transactional
public class PaymentService {

    @Autowired
    private ReservationRepository reservationRepository;

    @Autowired
    private EmailService emailService;

    @Value("${payment.bank.account}")
    private String bankAccount;

    @Value("${payment.bank.holder}")
    private String bankHolder;

    /**
     * 예매 생성 (결제 전)
     */
    public Reservation createReservation(Event event, String customerName,
                                         String customerEmail, String customerPhone,
                                         Integer ticketCount, PaymentMethod paymentMethod) {

        // 티켓 수량 체크
        if (event.getAvailableTickets() < ticketCount) {
            throw new RuntimeException("티켓이 매진되었습니다.");
        }

        Reservation reservation = new Reservation(event, customerName, customerEmail,
                customerPhone, ticketCount, paymentMethod);

        Reservation savedReservation = reservationRepository.save(reservation);

        // 예매 확인 이메일 발송
        emailService.sendReservationConfirmationEmail(
                customerEmail,
                savedReservation.getTicketNumber(),
                event.getTitle(),
                event.getEventDate().toString()
        );

        return savedReservation;
    }

    /**
     * 토스페이 개인 송금 링크 생성
     */
    public Map<String, Object> createTossPaymentLink(Reservation reservation) {
        Map<String, Object> paymentInfo = new HashMap<>();

        // 토스페이 개인 송금 링크 (실제로는 토스 API 연동 필요)
        String tossLink = "https://toss.me/" + "your_toss_id" + "/" + reservation.getTotalPrice();

        paymentInfo.put("paymentUrl", tossLink);
        paymentInfo.put("amount", reservation.getTotalPrice());
        paymentInfo.put("orderId", reservation.getTicketNumber());
        paymentInfo.put("method", "토스페이 송금");
        paymentInfo.put("description", reservation.getEvent().getTitle() + " 티켓 " + reservation.getTicketCount() + "매");

        return paymentInfo;
    }

    /**
     * 카카오페이 송금 정보 생성
     */
    public Map<String, Object> createKakaoPaymentInfo(Reservation reservation) {
        Map<String, Object> paymentInfo = new HashMap<>();

        // 카카오페이 QR 코드나 송금 링크 (실제 구현 시 카카오페이 API 연동)
        paymentInfo.put("qrCode", "/images/kakaopay-qr.png"); // QR 코드 이미지
        paymentInfo.put("amount", reservation.getTotalPrice());
        paymentInfo.put("orderId", reservation.getTicketNumber());
        paymentInfo.put("method", "카카오페이 송금");
        paymentInfo.put("description", "카카오페이로 " + reservation.getTotalPrice() + "원을 송금해주세요");

        return paymentInfo;
    }

    /**
     * 계좌이체 정보 생성
     */
    public Map<String, Object> createBankTransferInfo(Reservation reservation) {
        Map<String, Object> paymentInfo = new HashMap<>();

        paymentInfo.put("bankName", "국민은행");
        paymentInfo.put("accountNumber", bankAccount);
        paymentInfo.put("accountHolder", bankHolder);
        paymentInfo.put("amount", reservation.getTotalPrice());
        paymentInfo.put("depositorName", reservation.getTicketNumber()); // 입금자명을 티켓번호로
        paymentInfo.put("method", "계좌이체");
        paymentInfo.put("description", "입금자명을 반드시 '" + reservation.getTicketNumber() + "'로 입력해주세요");

        return paymentInfo;
    }

    /**
     * 결제 완료 처리 (관리자가 수동으로 확인 후 호출)
     */
    public void confirmPayment(Long reservationId, String paymentId) {
        Reservation reservation = reservationRepository.findById(reservationId)
                .orElseThrow(() -> new RuntimeException("예매를 찾을 수 없습니다."));

        if (reservation.getPaymentStatus() == PaymentStatus.COMPLETED) {
            throw new RuntimeException("이미 결제 완료된 예매입니다.");
        }

        // 이벤트 판매 수량 증가
        Event event = reservation.getEvent();
        event.setSoldTickets(event.getSoldTickets() + reservation.getTicketCount());

        // 예매 상태 업데이트
        reservation.setPaymentStatus(PaymentStatus.COMPLETED);
        reservation.setPaymentId(paymentId);
        reservation.setPaidAt(LocalDateTime.now());

        reservationRepository.save(reservation);

        // 결제 완료 이메일 발송
        sendTicketEmail(reservation);
    }

    /**
     * 티켓번호로 결제 완료 처리 (Webhook용)
     */
    public void confirmPaymentByTicketNumber(String ticketNumber, String paymentId) {
        Reservation reservation = reservationRepository.findByTicketNumber(ticketNumber)
                .orElseThrow(() -> new RuntimeException("유효하지 않은 티켓번호입니다."));

        confirmPayment(reservation.getId(), paymentId);
    }

    /**
     * 결제 실패 처리
     */
    public void failPayment(Long reservationId, String reason) {
        Reservation reservation = reservationRepository.findById(reservationId)
                .orElseThrow(() -> new RuntimeException("예매를 찾을 수 없습니다."));

        reservation.setPaymentStatus(PaymentStatus.FAILED);
        reservationRepository.save(reservation);

        // 결제 실패 이메일 발송 (선택적)
        sendPaymentFailEmail(reservation, reason);
    }

    /**
     * 결제 취소 처리
     */
    public void cancelPayment(Long reservationId, String reason) {
        Reservation reservation = reservationRepository.findById(reservationId)
                .orElseThrow(() -> new RuntimeException("예매를 찾을 수 없습니다."));

        if (reservation.getPaymentStatus() == PaymentStatus.COMPLETED) {
            // 이미 판매된 티켓 수량 복구
            Event event = reservation.getEvent();
            event.setSoldTickets(event.getSoldTickets() - reservation.getTicketCount());
        }

        reservation.setPaymentStatus(PaymentStatus.CANCELLED);
        reservationRepository.save(reservation);
    }

    /**
     * 티켓 이메일 발송
     */
    private void sendTicketEmail(Reservation reservation) {
        emailService.sendPaymentConfirmationEmail(
                reservation.getCustomerEmail(),
                reservation.getTicketNumber(),
                reservation.getEvent().getTitle(),
                reservation.getEvent().getEventDate().toString()
        );
    }

    /**
     * 결제 실패 이메일 발송
     */
    private void sendPaymentFailEmail(Reservation reservation, String reason) {
        String subject = "[결제 실패] " + reservation.getEvent().getTitle() + " 예매";
        String content = buildPaymentFailEmailContent(reservation, reason);

        emailService.sendEmail(reservation.getCustomerEmail(), subject, content);
    }

    private String buildPaymentFailEmailContent(Reservation reservation, String reason) {
        StringBuilder content = new StringBuilder();
        content.append("<h2>결제 처리 중 문제가 발생했습니다</h2>");
        content.append("<div style='border: 1px solid #dc3545; padding: 20px; margin: 20px 0; background-color: #f8d7da;'>");
        content.append("<h3>예매 정보</h3>");
        content.append("<p><strong>티켓번호:</strong> ").append(reservation.getTicketNumber()).append("</p>");
        content.append("<p><strong>이벤트:</strong> ").append(reservation.getEvent().getTitle()).append("</p>");
        content.append("<p><strong>실패 사유:</strong> ").append(reason).append("</p>");
        content.append("</div>");
        content.append("<p>문제 해결을 위해 고객센터로 연락 부탁드립니다.</p>");

        return content.toString();
    }

    /**
     * 입장 처리
     */
    public void processEntry(String ticketNumber) {
        Reservation reservation = reservationRepository.findByTicketNumber(ticketNumber)
                .orElseThrow(() -> new RuntimeException("유효하지 않은 티켓번호입니다."));

        if (reservation.getPaymentStatus() != PaymentStatus.COMPLETED) {
            throw new RuntimeException("결제가 완료되지 않은 티켓입니다.");
        }

        if (reservation.getUsed()) {
            throw new RuntimeException("이미 사용된 티켓입니다. (사용일시: " +
                    reservation.getUsedAt().toString() + ")");
        }

        // 이벤트 날짜 확인 (당일만 입장 가능하도록 설정 가능)
        LocalDateTime eventDate = reservation.getEvent().getEventDate();
        LocalDateTime now = LocalDateTime.now();

        // 이벤트 시작 2시간 전부터 입장 가능하도록 설정
        if (now.isBefore(eventDate.minusHours(2))) {
            throw new RuntimeException("아직 입장 시간이 아닙니다. 이벤트 시작 2시간 전부터 입장 가능합니다.");
        }

        reservation.setUsed(true);
        reservation.setUsedAt(LocalDateTime.now());
        reservationRepository.save(reservation);
    }

    /**
     * 티켓 유효성 검증
     */
    public boolean isValidTicket(String ticketNumber) {
        try {
            Reservation reservation = reservationRepository.findByTicketNumber(ticketNumber)
                    .orElse(null);

            if (reservation == null) {
                return false;
            }

            return reservation.getPaymentStatus() == PaymentStatus.COMPLETED && !reservation.getUsed();
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * 예매 통계 조회
     */
    public Map<String, Object> getReservationStats() {
        Map<String, Object> stats = new HashMap<>();

        long totalReservations = reservationRepository.count();
        long completedReservations = reservationRepository.findByPaymentStatus(PaymentStatus.COMPLETED).size();
        long pendingReservations = reservationRepository.findByPaymentStatus(PaymentStatus.PENDING).size();
        Long totalSales = reservationRepository.getTotalSales();

        stats.put("totalReservations", totalReservations);
        stats.put("completedReservations", completedReservations);
        stats.put("pendingReservations", pendingReservations);
        stats.put("totalSales", totalSales != null ? totalSales : 0L);
        stats.put("completionRate", totalReservations > 0 ?
                (double) completedReservations / totalReservations * 100 : 0.0);

        return stats;
    }
}