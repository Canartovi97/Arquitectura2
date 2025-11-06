package co.ucentral.microservices.parking_microservice.domain.parkingSpace;

import co.ucentral.microservices.parking_microservice.domain.location.LocationDto;
import jakarta.validation.Valid;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
import java.util.List;

@Builder
public record NewParkingRequest(


        @NotBlank(message = "Title is required")
        String title,

        @NotBlank(message = "Description is required")
        String description,

        @NotNull(message = "Hourly price is required")

        @DecimalMin(value = "0.01", message = "Price must be greater than 0")
        BigDecimal pricePerHour,

        @NotBlank(message = "Status is required")
        String status,

        @Valid
        @NotNull(message = "Location is required")
        LocationDto location,

        @NotNull(message = "Owner ID is required")
        Long ownerId

//        List<MultipartFile> images

) {
}
