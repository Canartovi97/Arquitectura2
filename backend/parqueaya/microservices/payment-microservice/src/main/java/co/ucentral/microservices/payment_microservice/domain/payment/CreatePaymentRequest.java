package co.ucentral.microservices.payment_microservice.domain.payment;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;
import java.util.UUID;

public record CreatePaymentRequest(

        @NotNull(message = "Booking ID is required")
        UUID bookingId,

        @NotNull(message = "User ID is required")
        Long userId,

        @NotNull(message = "Amount is required")
        @Positive(message = "Amount must be positive")
        BigDecimal amount,

        @NotNull(message = "Currency is required")
        String currency,

        @NotNull(message = "Description is required")
        String description

) {
}
