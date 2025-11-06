package co.ucentral.microservices.booking_microservice.client;

import java.math.BigDecimal;

public record ParkingSpaceDto(
        Long id,
        BigDecimal pricePerHour
) {
}
