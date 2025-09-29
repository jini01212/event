package com.example.eventplatform.config;

import com.example.eventplatform.service.EventService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

@Component
public class ApplicationStartupRunner implements ApplicationRunner {

    @Autowired
    private EventService eventService;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        System.out.println("Event Platform 애플리케이션이 시작되었습니다!");
        System.out.println("사용자 페이지: http://localhost:8080");
        System.out.println("관리자 페이지: http://localhost:8080/admin");
        System.out.println("현재 등록된 이벤트 수: " + eventService.getAllEvents().size());
        System.out.println("============================================");
    }
}