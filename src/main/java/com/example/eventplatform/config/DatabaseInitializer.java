package com.example.eventplatform.config;

import com.example.eventplatform.entity.Event;
import com.example.eventplatform.service.EventService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class DatabaseInitializer implements CommandLineRunner {

    @Autowired
    private EventService eventService;

    @Override
    public void run(String... args) throws Exception {
        // 개발 환경에서만 샘플 데이터 생성
        if (eventService.getAllEvents().isEmpty()) {
            createSampleEvents();
        }
    }

    private void createSampleEvents() {
        // 샘플 이벤트 1
        Event event1 = new Event(
                "2024 New Year Concert",
                "새해를 맞이하는 특별한 콘서트입니다. 유명 아티스트들의 화려한 공연을 즐기세요!",
                "서울 올림픽공원 체조경기장",
                LocalDateTime.now().plusDays(30),
                50000,
                100
        );

        // 샘플 이벤트 2
        Event event2 = new Event(
                "스타트업 네트워킹 세미나",
                "스타트업 창업자들을 위한 네트워킹 세미나입니다. 성공한 창업가들의 경험담을 들어보세요.",
                "강남 코엑스 컨퍼런스룸",
                LocalDateTime.now().plusDays(15),
                30000,
                50
        );

        // 샘플 이벤트 3
        Event event3 = new Event(
                "요리 클래스 - 이탈리안 파스타",
                "전문 셰프와 함께하는 이탈리안 파스타 만들기 클래스입니다.",
                "홍대 요리학원",
                LocalDateTime.now().plusDays(7),
                80000,
                15
        );

        try {
            eventService.createEvent(event1, null);
            eventService.createEvent(event2, null);
            eventService.createEvent(event3, null);

            System.out.println("샘플 이벤트가 생성되었습니다.");
        } catch (Exception e) {
            System.err.println("샘플 이벤트 생성 중 오류 발생: " + e.getMessage());
        }
    }
}