package com.example.eventplatform.util;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;

public class DateTimeUtil {

    public static final DateTimeFormatter DISPLAY_FORMATTER = DateTimeFormatter.ofPattern("yyyy년 MM월 dd일 HH:mm");
    public static final DateTimeFormatter SHORT_FORMATTER = DateTimeFormatter.ofPattern("MM/dd HH:mm");
    public static final DateTimeFormatter ISO_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm");

    /**
     * 표시용 날짜 형식으로 변환
     */
    public static String formatForDisplay(LocalDateTime dateTime) {
        return dateTime.format(DISPLAY_FORMATTER);
    }

    /**
     * 짧은 형식으로 변환
     */
    public static String formatShort(LocalDateTime dateTime) {
        return dateTime.format(SHORT_FORMATTER);
    }

    /**
     * HTML datetime-local input용 형식으로 변환
     */
    public static String formatForInput(LocalDateTime dateTime) {
        return dateTime.format(ISO_FORMATTER);
    }

    /**
     * 현재 시간으로부터 상대적 시간 계산
     */
    public static String getRelativeTime(LocalDateTime dateTime) {
        LocalDateTime now = LocalDateTime.now();
        long minutes = ChronoUnit.MINUTES.between(dateTime, now);

        if (minutes < 1) {
            return "방금 전";
        } else if (minutes < 60) {
            return minutes + "분 전";
        } else if (minutes < 1440) { // 24시간
            return (minutes / 60) + "시간 전";
        } else {
            return (minutes / 1440) + "일 전";
        }
    }

    /**
     * 이벤트까지 남은 시간 계산
     */
    public static String getTimeUntilEvent(LocalDateTime eventDate) {
        LocalDateTime now = LocalDateTime.now();

        if (eventDate.isBefore(now)) {
            return "종료됨";
        }

        long days = ChronoUnit.DAYS.between(now, eventDate);
        long hours = ChronoUnit.HOURS.between(now, eventDate) % 24;
        long minutes = ChronoUnit.MINUTES.between(now, eventDate) % 60;

        if (days > 0) {
            return days + "일 " + hours + "시간 후";
        } else if (hours > 0) {
            return hours + "시간 " + minutes + "분 후";
        } else {
            return minutes + "분 후";
        }
    }
}
