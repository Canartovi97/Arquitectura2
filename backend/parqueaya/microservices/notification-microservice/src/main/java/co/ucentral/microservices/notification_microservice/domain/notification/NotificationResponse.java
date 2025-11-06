package co.ucentral.microservices.notification_microservice.domain.notification;

import lombok.Builder;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.UUID;

@Builder
public record NotificationResponse(
        UUID id,
        Long userId,
        NotificationType type,
        String title,
        String message,
        NotificationStatus status,
        NotificationChannel channel,
        LocalDateTime sentAt,
        LocalDateTime readAt,
        Map<String, Object> metadata,
        LocalDateTime createdAt
) {
}
