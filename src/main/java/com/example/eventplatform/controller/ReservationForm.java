package com.example.eventplatform.controller;

import com.example.eventplatform.entity.PaymentMethod;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Min;

public class ReservationForm {

    @NotBlank(message = "예매자명을 입력해주세요")
    private String customerName;

    @NotBlank(message = "이메일을 입력해주세요")
    @Email(message = "올바른 이메일 형식이 아닙니다")
    private String customerEmail;

    @NotBlank(message = "연락처를 입력해주세요")
    private String customerPhone;

    @NotNull(message = "티켓 수량을 선택해주세요")
    @Min(value = 1, message = "최소 1매 이상 선택해야 합니다")
    private Integer ticketCount = 1;

    @NotNull(message = "결제 방식을 선택해주세요")
    private PaymentMethod paymentMethod;

    // 생성자
    public ReservationForm() {}

    public ReservationForm(String customerName, String customerEmail, String customerPhone,
                           Integer ticketCount, PaymentMethod paymentMethod) {
        this.customerName = customerName;
        this.customerEmail = customerEmail;
        this.customerPhone = customerPhone;
        this.ticketCount = ticketCount;
        this.paymentMethod = paymentMethod;
    }

    // Getters and Setters
    public String getCustomerName() { return customerName; }
    public void setCustomerName(String customerName) { this.customerName = customerName; }

    public String getCustomerEmail() { return customerEmail; }
    public void setCustomerEmail(String customerEmail) { this.customerEmail = customerEmail; }

    public String getCustomerPhone() { return customerPhone; }
    public void setCustomerPhone(String customerPhone) { this.customerPhone = customerPhone; }

    public Integer getTicketCount() { return ticketCount; }
    public void setTicketCount(Integer ticketCount) { this.ticketCount = ticketCount; }

    public PaymentMethod getPaymentMethod() { return paymentMethod; }
    public void setPaymentMethod(PaymentMethod paymentMethod) { this.paymentMethod = paymentMethod; }
}