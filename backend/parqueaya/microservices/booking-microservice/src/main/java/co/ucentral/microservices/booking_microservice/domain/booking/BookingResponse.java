package co.ucentral.microservices.booking_microservice.domain.booking;

import lombok.Builder;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Builder
public record BookingResponse(

        UUID id,
        Long parkingSpaceId,
        Long userId,
        LocalDateTime startTime,
        LocalDateTime endTime,
        BigDecimal totalAmount,
        BookingStatus status,
        LocalDateTime createdAt,
        LocalDateTime cancelledAt,
        String cancellationReason


) {
}
