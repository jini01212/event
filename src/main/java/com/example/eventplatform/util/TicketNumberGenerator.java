package com.example.eventplatform.util;

import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Random;

@Component
public class TicketNumberGenerator {

    private static final String PREFIX = "TK";
    private static final Random random = new Random();

    /**
     * 고유한 티켓번호 생성
     * 형식: TK + YYMMDD + HHMMSS + 랜덤3자리
     */
    public String generate() {
        LocalDateTime now = LocalDateTime.now();
        String dateTime = now.format(DateTimeFormatter.ofPattern("yyMMddHHmmss"));
        String randomNum = String.format("%03d", random.nextInt(1000));

        return PREFIX + dateTime + randomNum;
    }

    /**
     * 간단한 티켓번호 생성 (기존 방식)
     */
    public String generateSimple() {
        String timestamp = String.valueOf(System.currentTimeMillis());
        String randomNum = String.valueOf(random.nextInt(1000));
        return PREFIX + timestamp.substring(timestamp.length() - 6) + String.format("%03d", Integer.parseInt(randomNum));
    }
}