package com.example.eventplatform.util;

import java.util.regex.Pattern;

public class ValidationUtil {

    private static final Pattern EMAIL_PATTERN =
            Pattern.compile("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$");

    private static final Pattern PHONE_PATTERN =
            Pattern.compile("^01[0-9]-\\d{3,4}-\\d{4}$");

    private static final Pattern TICKET_NUMBER_PATTERN =
            Pattern.compile("^TK\\d{12,15}$");

    /**
     * 이메일 형식 검증
     */
    public static boolean isValidEmail(String email) {
        return email != null && EMAIL_PATTERN.matcher(email).matches();
    }

    /**
     * 전화번호 형식 검증
     */
    public static boolean isValidPhone(String phone) {
        return phone != null && PHONE_PATTERN.matcher(phone).matches();
    }

    /**
     * 티켓번호 형식 검증
     */
    public static boolean isValidTicketNumber(String ticketNumber) {
        return ticketNumber != null && TICKET_NUMBER_PATTERN.matcher(ticketNumber).matches();
    }

    /**
     * 전화번호 자동 포맷팅
     */
    public static String formatPhone(String phone) {
        if (phone == null) return "";

        String numbers = phone.replaceAll("[^0-9]", "");

        if (numbers.length() == 11 && numbers.startsWith("010")) {
            return numbers.substring(0, 3) + "-" +
                    numbers.substring(3, 7) + "-" +
                    numbers.substring(7);
        } else if (numbers.length() == 10 && numbers.startsWith("01")) {
            return numbers.substring(0, 3) + "-" +
                    numbers.substring(3, 6) + "-" +
                    numbers.substring(6);
        }

        return phone;
    }
}