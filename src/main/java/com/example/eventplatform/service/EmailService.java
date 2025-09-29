package com.example.eventplatform.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;

    /**
     * HTML 이메일 발송
     */
    public void sendEmail(String to, String subject, String content) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(content, true); // HTML 형식
            helper.setFrom("noreply@eventplatform.com");

            mailSender.send(message);

        } catch (MessagingException e) {
            throw new RuntimeException("이메일 발송에 실패했습니다.", e);
        }
    }

    /**
     * 텍스트 이메일 발송
     */
    public void sendTextEmail(String to, String subject, String content) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(content, false); // 텍스트 형식
            helper.setFrom("noreply@eventplatform.com");

            mailSender.send(message);

        } catch (MessagingException e) {
            throw new RuntimeException("이메일 발송에 실패했습니다.", e);
        }
    }

    /**
     * 예매 확인 이메일 발송
     */
    public void sendReservationConfirmationEmail(String to, String ticketNumber, String eventTitle, String eventDate) {
        String subject = "[예매 확인] " + eventTitle;
        String content = buildReservationEmailContent(ticketNumber, eventTitle, eventDate);
        sendEmail(to, subject, content);
    }

    /**
     * 결제 완료 이메일 발송
     */
    public void sendPaymentConfirmationEmail(String to, String ticketNumber, String eventTitle, String eventDate) {
        String subject = "[결제 완료] " + eventTitle + " 티켓이 발급되었습니다";
        String content = buildPaymentConfirmationEmailContent(ticketNumber, eventTitle, eventDate);
        sendEmail(to, subject, content);
    }

    private String buildReservationEmailContent(String ticketNumber, String eventTitle, String eventDate) {
        StringBuilder content = new StringBuilder();
        content.append("<h2>예매가 접수되었습니다!</h2>");
        content.append("<p>안녕하세요. 예매해주셔서 감사합니다.</p>");
        content.append("<div style='border: 1px solid #ddd; padding: 20px; margin: 20px 0;'>");
        content.append("<h3>예매 정보</h3>");
        content.append("<p><strong>티켓번호:</strong> ").append(ticketNumber).append("</p>");
        content.append("<p><strong>이벤트:</strong> ").append(eventTitle).append("</p>");
        content.append("<p><strong>일시:</strong> ").append(eventDate).append("</p>");
        content.append("</div>");
        content.append("<p><strong>결제 확인 후 티켓이 정식 발급됩니다.</strong></p>");
        content.append("<p>문의사항이 있으시면 언제든 연락주세요.</p>");

        return content.toString();
    }

    private String buildPaymentConfirmationEmailContent(String ticketNumber, String eventTitle, String eventDate) {
        StringBuilder content = new StringBuilder();
        content.append("<h2>🎉 티켓이 발급되었습니다!</h2>");
        content.append("<p>결제가 완료되어 티켓이 정식 발급되었습니다.</p>");
        content.append("<div style='border: 2px solid #007bff; padding: 20px; margin: 20px 0; background-color: #f8f9fa;'>");
        content.append("<h3>🎫 티켓 정보</h3>");
        content.append("<p><strong>티켓번호:</strong> <span style='font-family: monospace; font-size: 1.2em; color: #007bff;'>").append(ticketNumber).append("</span></p>");
        content.append("<p><strong>이벤트:</strong> ").append(eventTitle).append("</p>");
        content.append("<p><strong>일시:</strong> ").append(eventDate).append("</p>");
        content.append("</div>");
        content.append("<div style='background-color: #fff3cd; border: 1px solid #ffeaa7; padding: 15px; margin: 20px 0;'>");
        content.append("<p style='margin: 0;'><strong>⚠️ 중요:</strong> 현장에서 위 티켓번호를 제시해주세요!</p>");
        content.append("</div>");
        content.append("<p>즐거운 이벤트 되세요! 🎊</p>");

        return content.toString();
    }
}