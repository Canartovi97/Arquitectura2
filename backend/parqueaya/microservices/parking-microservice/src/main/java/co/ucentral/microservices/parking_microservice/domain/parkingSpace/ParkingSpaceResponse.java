package co.ucentral.microservices.parking_microservice.domain.parkingSpace;

import co.ucentral.microservices.parking_microservice.domain.location.LocationDto;
import lombok.Builder;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Builder
public record ParkingSpaceResponse(

        Long id,
        Long ownerId,
        String title,
        String description,
        BigDecimal pricePerHour,
        String status, // o enum si prefieres
        LocationDto location,
        LocalDateTime createdAt,
        List<String> imageUrls
) {
}
