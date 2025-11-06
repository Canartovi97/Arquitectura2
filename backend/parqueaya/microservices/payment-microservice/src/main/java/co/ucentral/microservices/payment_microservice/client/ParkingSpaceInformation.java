package co.ucentral.microservices.payment_microservice.client;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public record ParkingSpaceInformation(
        Long id,
        Long ownerId,
        String title,
        String description,
        BigDecimal pricePerHour,
        String status, // o enum si prefieres
        Object location,
        LocalDateTime createdAt,
        List<String> imageUrls
) {
}
