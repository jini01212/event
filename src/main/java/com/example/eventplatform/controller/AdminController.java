package com.example.eventplatform.controller;

import com.example.eventplatform.entity.*;
import com.example.eventplatform.service.EventService;
import com.example.eventplatform.service.PaymentService;
import com.example.eventplatform.service.ReservationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private EventService eventService;

    @Autowired
    private PaymentService paymentService;

    @Autowired
    private ReservationService reservationService;

    /**
     * 관리자 메인 페이지
     */
    @GetMapping("")
    public String adminHome(Model model) {
        List<Event> events = eventService.getAllEvents();
        List<Reservation> pendingReservations = reservationService.getPendingReservations();

        model.addAttribute("events", events);
        model.addAttribute("pendingReservations", pendingReservations);
        return "admin/dashboard";
    }

    /**
     * 이벤트 등록 폼
     */
    @GetMapping("/event/new")
    public String newEventForm(Model model) {
        model.addAttribute("event", new Event());
        return "admin/event-form";
    }

    /**
     * 이벤트 등록 처리
     */
    @PostMapping("/event/new")
    public String createEvent(@ModelAttribute Event event,
                              @RequestParam(required = false) MultipartFile imageFile,
                              RedirectAttributes redirectAttributes) {
        try {
            eventService.createEvent(event, imageFile);
            redirectAttributes.addFlashAttribute("success", "이벤트가 등록되었습니다.");
            return "redirect:/admin";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/admin/event/new";
        }
    }

    /**
     * 이벤트 수정 폼
     */
    @GetMapping("/event/{id}/edit")
    public String editEventForm(@PathVariable Long id, Model model) {
        Event event = eventService.getEvent(id);
        model.addAttribute("event", event);
        return "admin/event-form";
    }

    /**
     * 이벤트 수정 처리
     */
    @PostMapping("/event/{id}/edit")
    public String updateEvent(@PathVariable Long id,
                              @ModelAttribute Event event,
                              @RequestParam(required = false) MultipartFile imageFile,
                              RedirectAttributes redirectAttributes) {
        try {
            event.setId(id);
            eventService.updateEvent(event, imageFile);
            redirectAttributes.addFlashAttribute("success", "이벤트가 수정되었습니다.");
            return "redirect:/admin";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/admin/event/" + id + "/edit";
        }
    }

    /**
     * 이벤트 삭제/비활성화
     */
    @PostMapping("/event/{id}/deactivate")
    public String deactivateEvent(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            eventService.deactivateEvent(id);
            redirectAttributes.addFlashAttribute("success", "이벤트가 비활성화되었습니다.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/admin";
    }

    /**
     * 예매 관리 페이지
     */
    @GetMapping("/reservations")
    public String reservations(@RequestParam(required = false) String status, Model model) {
        List<Reservation> reservations;

        if (status != null && !status.isEmpty()) {
            try {
                PaymentStatus paymentStatus = PaymentStatus.valueOf(status.toUpperCase());
                reservations = reservationService.getReservationsByStatus(paymentStatus);
            } catch (IllegalArgumentException e) {
                reservations = reservationService.getAllReservations();
            }
        } else {
            reservations = reservationService.getAllReservations();
        }

        model.addAttribute("reservations", reservations);
        model.addAttribute("currentStatus", status);
        return "admin/reservations";
    }

    /**
     * 예매 상세 보기
     */
    @GetMapping("/reservation/{id}")
    public String reservationDetail(@PathVariable Long id, Model model) {
        Reservation reservation = reservationService.getReservation(id);
        model.addAttribute("reservation", reservation);
        return "admin/reservation-detail";
    }

    /**
     * 결제 확인 처리
     */
    @PostMapping("/reservation/{id}/confirm")
    public String confirmPayment(@PathVariable Long id,
                                 @RequestParam String paymentId,
                                 RedirectAttributes redirectAttributes) {
        try {
            paymentService.confirmPayment(id, paymentId);
            redirectAttributes.addFlashAttribute("success", "결제가 확인되었습니다.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/admin/reservations";
    }

    /**
     * 결제 실패 처리
     */
    @PostMapping("/reservation/{id}/fail")
    public String failPayment(@PathVariable Long id,
                              @RequestParam String reason,
                              RedirectAttributes redirectAttributes) {
        try {
            paymentService.failPayment(id, reason);
            redirectAttributes.addFlashAttribute("success", "결제 실패로 처리되었습니다.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/admin/reservations";
    }

    /**
     * 입장 처리 페이지
     */
    @GetMapping("/entry")
    public String entryPage(Model model) {
        return "admin/entry";
    }

    /**
     * 입장 처리
     */
    @PostMapping("/entry")
    public String processEntry(@RequestParam String ticketNumber,
                               RedirectAttributes redirectAttributes) {
        try {
            paymentService.processEntry(ticketNumber);
            redirectAttributes.addFlashAttribute("success",
                    "티켓번호 " + ticketNumber + "의 입장 처리가 완료되었습니다.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/admin/entry";
    }

    /**
     * 티켓 번호로 예매 조회 (AJAX)
     */
    @GetMapping("/reservation/search")
    @ResponseBody
    public Reservation searchReservation(@RequestParam String ticketNumber) {
        try {
            return reservationService.getReservationByTicketNumber(ticketNumber);
        } catch (Exception e) {
            return null; // 프론트엔드에서 null 체크로 처리
        }
    }

    /**
     * 통계 데이터 조회 (AJAX)
     */
    @GetMapping("/api/stats")
    @ResponseBody
    public AdminStats getAdminStats() {
        return new AdminStats(
                eventService.getAllEvents().size(),
                reservationService.getPendingReservations().size(),
                reservationService.getTotalSales(),
                reservationService.getTodayReservations().size()
        );
    }

    /**
     * 이벤트별 예매 현황 (AJAX)
     */
    @GetMapping("/api/event/{id}/reservations")
    @ResponseBody
    public List<Reservation> getEventReservations(@PathVariable Long id) {
        return reservationService.getReservationsByEvent(id);
    }
}