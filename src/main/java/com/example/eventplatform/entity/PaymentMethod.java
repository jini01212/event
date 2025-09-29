package com.example.eventplatform.entity;

public enum PaymentMethod {
    TOSS("토스페이"),
    KAKAO("카카오페이"),
    BANK_TRANSFER("계좌이체");

    private final String description;

    PaymentMethod(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
