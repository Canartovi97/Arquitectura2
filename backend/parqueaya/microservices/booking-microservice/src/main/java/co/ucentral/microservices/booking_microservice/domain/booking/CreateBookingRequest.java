package co.ucentral.microservices.booking_microservice.domain.booking;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record CreateBookingRequest(

        @NotNull(message = "Parking space ID is required")
        Long parkingSpaceId,

        @NotNull(message = "User ID is required")
        Long userId,

        @Future(message = "Start time must be in the future")
        @NotNull(message = "Start time is required")
        LocalDateTime startTime,

        @Future(message = "End time must be in the future")
        @NotNull(message = "End time is required")
        LocalDateTime endTime
) {
}
