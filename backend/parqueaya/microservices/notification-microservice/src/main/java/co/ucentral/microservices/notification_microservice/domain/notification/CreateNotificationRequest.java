package co.ucentral.microservices.notification_microservice.domain.notification;

import lombok.Builder;

import java.util.Map;

@Builder
public record CreateNotificationRequest(
        Long userId,
        NotificationType type,
        String title,
        String message,
        NotificationChannel channel,
        Map<String, Object> metadata
) {
}
