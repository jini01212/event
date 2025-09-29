package com.example.eventplatform.constant;

public class Constants {

    // 결제 관련 상수
    public static final int PAYMENT_TIMEOUT_MINUTES = 10;
    public static final String DEFAULT_PAYMENT_ID_PREFIX = "PAY_";

    // 티켓 관련 상수
    public static final String TICKET_NUMBER_PREFIX = "TK";
    public static final int MAX_TICKET_COUNT_PER_ORDER = 10;

    // 파일 업로드 관련 상수
    public static final long MAX_FILE_SIZE = 10 * 1024 * 1024; // 10MB
    public static final String[] ALLOWED_IMAGE_EXTENSIONS = {".jpg", ".jpeg", ".png", ".gif", ".webp"};

    // 이메일 관련 상수
    public static final String EMAIL_SUBJECT_PREFIX = "[EventPlatform] ";
    public static final String NO_REPLY_EMAIL = "noreply@eventplatform.com";

    // 시간 관련 상수
    public static final int ENTRY_ALLOWED_HOURS_BEFORE = 2; // 이벤트 시작 2시간 전부터 입장 가능

    // 페이지네이션 관련 상수
    public static final int DEFAULT_PAGE_SIZE = 20;
    public static final int MAX_PAGE_SIZE = 100;
}
