package co.ucentral.microservices.parking_microservice.configuration.mapper;

import co.ucentral.microservices.parking_microservice.domain.location.Location;
import co.ucentral.microservices.parking_microservice.domain.location.LocationDto;
import org.springframework.stereotype.Component;

@Component
public class LocationMapper {

    public Location toLocation(LocationDto locationDto){
        return  Location.builder()
                .address(locationDto.address())
                .city(locationDto.city())
                .neighborhood(locationDto.neighborhood())
                .latitude(locationDto.latitude())
                .longitude(locationDto.longitude())
                .build();
    }

    public LocationDto toLocationDto(Location location){
        return LocationDto.builder()
                .address(location.getAddress())
                .city(location.getCity())
                .neighborhood(location.getNeighborhood())
                .latitude(location.getLatitude())
                .longitude(location.getLongitude())
                .build();
    }


}
