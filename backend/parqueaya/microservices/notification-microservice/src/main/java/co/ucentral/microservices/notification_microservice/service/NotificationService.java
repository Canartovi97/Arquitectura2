package co.ucentral.microservices.notification_microservice.service;

import co.ucentral.microservices.notification_microservice.configuration.mapper.NotificationMapper;
import co.ucentral.microservices.notification_microservice.domain.notification.*;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class NotificationService {

//    private final NotificationRepository notificationRepository;
//    private final NotificationMapper notificationMapper;
//
//    @Transactional
//    public NotificationResponse createNotification(CreateNotificationRequest request) {
//        Notification notification = NotificationMapper.toEntity(request);
//        Notification saved = notificationRepository.save(notification);
//        return NotificationMapper.toResponseDto(saved);
//    }
//
//    public Page<NotificationResponse> getNotificationsByUser(Long userId, Pageable pageable) {
//        return notificationRepository.findByUserIdOrderByCreatedAtDesc(userId, pageable)
//                .map(NotificationMapper::toResponseDto);
//    }
//
//    @Transactional
//    public NotificationResponse markAsRead(UUID id, Long userId) {
//        Notification notification = notificationRepository.findById(id)
//                .orElseThrow(() -> new RuntimeException("Notification not found with id: " + id));
//
//        // Validar que el usuario sea el dueño de la notificación
//        if (!notification.getUserId().equals(userId)) {
//            throw new SecurityException("You don't have permission to read this notification");
//        }
//
//        notification.setStatus(NotificationStatus.READ);
//        notification.setReadAt(LocalDateTime.now());
//        Notification updated = notificationRepository.save(notification);
//        return NotificationMapper.toResponseDto(updated);
//    }



}
