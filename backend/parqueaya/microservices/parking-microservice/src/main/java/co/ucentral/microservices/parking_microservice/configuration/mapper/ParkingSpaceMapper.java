package co.ucentral.microservices.parking_microservice.configuration.mapper;

import co.ucentral.microservices.parking_microservice.domain.parkingSpace.NewParkingRequest;
import co.ucentral.microservices.parking_microservice.domain.parkingSpace.ParkingSpace;
import co.ucentral.microservices.parking_microservice.domain.parkingSpace.ParkingSpaceResponse;
import co.ucentral.microservices.parking_microservice.domain.parkingSpace.ParkingSpaceStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

@Component
@RequiredArgsConstructor
public class ParkingSpaceMapper {

    private final LocationMapper locationMapper;

    public ParkingSpace toParkingSpace(NewParkingRequest parkingRequest){
        return   ParkingSpace.builder()
                .ownerId(parkingRequest.ownerId())
                .title(parkingRequest.title())
                .description(parkingRequest.description())
                .pricePerHour(parkingRequest.pricePerHour())
                .status(ParkingSpaceStatus.valueOf(parkingRequest.status().toUpperCase()))
                .location(locationMapper.toLocation(parkingRequest.location()))
                .createdAt(LocalDateTime.now())
                .build();
    }

    public ParkingSpaceResponse toResponseDto(ParkingSpace parkingSpace, List<String> imageUrls) {

        if (parkingSpace == null) return null;

        return ParkingSpaceResponse.builder()
                .id(parkingSpace.getId())
                .ownerId(parkingSpace.getOwnerId())
                .title(parkingSpace.getTitle())
                .description(parkingSpace.getDescription())
                .pricePerHour(parkingSpace.getPricePerHour())
                .status(parkingSpace.getStatus().name())
                .location(locationMapper.toLocationDto(parkingSpace.getLocation()))
                .imageUrls(imageUrls)
                .createdAt(parkingSpace.getCreatedAt())
                .build();
    }



}
