package com.example.eventplatform.controller;

import com.example.eventplatform.entity.*;
import com.example.eventplatform.service.EventService;
import com.example.eventplatform.service.PaymentService;
import com.example.eventplatform.service.ReservationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.Map;

@Controller
public class EventController {

    @Autowired
    private EventService eventService;

    @Autowired
    private PaymentService paymentService;

    @Autowired
    private ReservationService reservationService;

    /**
     * 메인 페이지 - 이벤트 목록
     */
    @GetMapping("/")
    public String home(Model model) {
        List<Event> events = eventService.getActiveEvents();
        model.addAttribute("events", events);
        return "index";
    }

    /**
     * 이벤트 상세 페이지
     */
    @GetMapping("/event/{id}")
    public String eventDetail(@PathVariable Long id, Model model) {
        Event event = eventService.getEvent(id);
        model.addAttribute("event", event);
        model.addAttribute("reservation", new ReservationForm());
        return "event-detail";
    }

    /**
     * 예매 처리
     */
    @PostMapping("/event/{eventId}/book")
    public String bookEvent(@PathVariable Long eventId,
                            @ModelAttribute ReservationForm form,
                            RedirectAttributes redirectAttributes) {
        try {
            Event event = eventService.getEvent(eventId);

            // 예매 생성
            Reservation reservation = paymentService.createReservation(
                    event, form.getCustomerName(), form.getCustomerEmail(),
                    form.getCustomerPhone(), form.getTicketCount(), form.getPaymentMethod()
            );

            redirectAttributes.addAttribute("reservationId", reservation.getId());
            return "redirect:/payment";

        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/event/" + eventId;
        }
    }

    /**
     * 결제 페이지
     */
    @GetMapping("/payment")
    public String payment(@RequestParam Long reservationId, Model model) {
        Reservation reservation = reservationService.getReservation(reservationId);

        // 결제 방식에 따른 결제 정보 생성
        Map<String, Object> paymentInfo;
        switch (reservation.getPaymentMethod()) {
            case TOSS:
                paymentInfo = paymentService.createTossPaymentLink(reservation);
                break;
            case KAKAO:
                paymentInfo = paymentService.createKakaoPaymentInfo(reservation);
                break;
            case BANK_TRANSFER:
                paymentInfo = paymentService.createBankTransferInfo(reservation);
                break;
            default:
                throw new RuntimeException("지원하지 않는 결제 방식입니다.");
        }

        model.addAttribute("reservation", reservation);
        model.addAttribute("paymentInfo", paymentInfo);
        return "payment";
    }

    /**
     * 결제 완료 페이지
     */
    @GetMapping("/payment/complete")
    public String paymentComplete(@RequestParam Long reservationId, Model model) {
        Reservation reservation = reservationService.getReservation(reservationId);
        model.addAttribute("reservation", reservation);
        return "payment-complete";
    }

    /**
     * 티켓 조회
     */
    @GetMapping("/ticket/{ticketNumber}")
    public String ticketDetail(@PathVariable String ticketNumber, Model model) {
        Reservation reservation = reservationService.getReservationByTicketNumber(ticketNumber);
        model.addAttribute("reservation", reservation);
        return "ticket-detail";
    }
    /**
     * 마이페이지 - 내 예매 목록
     */
    @GetMapping("/my-tickets")
    public String myTickets(@RequestParam(required = false) String email, Model model) {
        if (email != null && !email.isEmpty()) {
            List<Reservation> reservations = reservationService.getReservationsByEmail(email);
            model.addAttribute("reservations", reservations);
        }
        model.addAttribute("email", email);
        return "my-tickets";
    }
    /**
     * 티켓 번호로 단일 조회 (AJAX용)
     */
    @GetMapping("/api/ticket/{ticketNumber}")
    @ResponseBody
    public Reservation getTicketInfo(@PathVariable String ticketNumber) {
        return reservationService.getReservationByTicketNumber(ticketNumber);
    }
}