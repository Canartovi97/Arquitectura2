package co.ucentral.microservices.parking_microservice.domain.location;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;

@Builder
public record LocationDto(

        @NotBlank(message = "Address is required")
        String address,

        @NotBlank(message = "City is required")
        String city,

        @NotBlank(message = "Neighborhood is required")
        String neighborhood,

//        @NotNull(message = "Latitude is required")
        Double latitude,

//        @NotNull(message = "Longitude is required")
        Double longitude


) {
}
