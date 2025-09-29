package com.example.eventplatform.service;

import com.example.eventplatform.entity.Event;
import com.example.eventplatform.repository.EventRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
public class EventService {

    @Autowired
    private EventRepository eventRepository;

    @Value("${file.upload-dir}")
    private String uploadDir;

    /**
     * 활성화된 이벤트 목록 조회
     */
    public List<Event> getActiveEvents() {
        return eventRepository.findByActiveTrueOrderByEventDateAsc();
    }

    /**
     * 모든 이벤트 조회 (관리자용)
     */
    public List<Event> getAllEvents() {
        return eventRepository.findAll();
    }

    /**
     * 이벤트 단일 조회
     */
    public Event getEvent(Long id) {
        return eventRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("이벤트를 찾을 수 없습니다."));
    }

    /**
     * 이벤트 등록
     */
    public Event createEvent(Event event, MultipartFile imageFile) {
        // 이미지 파일 업로드 처리
        if (imageFile != null && !imageFile.isEmpty()) {
            String imageUrl = uploadImage(imageFile);
            event.setImageUrl(imageUrl);
        }

        event.setCreatedAt(LocalDateTime.now());
        event.setActive(true);
        event.setSoldTickets(0);

        return eventRepository.save(event);
    }

    /**
     * 이벤트 수정
     */
    public Event updateEvent(Event event, MultipartFile imageFile) {
        Event existingEvent = getEvent(event.getId());

        // 기본 정보 업데이트
        existingEvent.setTitle(event.getTitle());
        existingEvent.setDescription(event.getDescription());
        existingEvent.setLocation(event.getLocation());
        existingEvent.setEventDate(event.getEventDate());
        existingEvent.setPrice(event.getPrice());
        existingEvent.setMaxTickets(event.getMaxTickets());

        // 이미지 파일 업로드 처리
        if (imageFile != null && !imageFile.isEmpty()) {
            String imageUrl = uploadImage(imageFile);
            existingEvent.setImageUrl(imageUrl);
        }

        return eventRepository.save(existingEvent);
    }

    /**
     * 이벤트 비활성화
     */
    public void deactivateEvent(Long id) {
        Event event = getEvent(id);
        event.setActive(false);
        eventRepository.save(event);
    }

    /**
     * 이미지 파일 업로드
     */
    private String uploadImage(MultipartFile file) {
        try {
            // 업로드 디렉토리 생성
            File uploadDirFile = new File(uploadDir);
            if (!uploadDirFile.exists()) {
                uploadDirFile.mkdirs();
            }

            // 파일명 생성 (중복 방지)
            String originalFilename = file.getOriginalFilename();
            String extension = originalFilename.substring(originalFilename.lastIndexOf("."));
            String filename = UUID.randomUUID().toString() + extension;

            // 파일 저장
            File dest = new File(uploadDir + filename);
            file.transferTo(dest);

            return "/uploads/" + filename;

        } catch (IOException e) {
            throw new RuntimeException("파일 업로드에 실패했습니다.", e);
        }
    }
}
