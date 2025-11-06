package co.ucentral.microservices.payment_microservice.client;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

public record BookingDto(
        UUID id,
        Long parkingSpaceId,
        Long userId,
        LocalDateTime startTime,
        LocalDateTime endTime,
        BigDecimal totalAmount,
        String status
) {
}
