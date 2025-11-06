package co.ucentral.microservices.booking_microservice.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

@FeignClient(name = "parking-service", url = "${parking.service.url:http://localhost:9092}")
public interface ParkingServiceClient {

    @GetMapping("/api/v1/parking-spaces/{id}")
    ParkingSpaceDto getParkingSpaceById(@PathVariable("id") Long id);

    @PutMapping("/api/v1/parking-spaces/{id}/status")
    void updateParkingSpaceStatus(
            @PathVariable("id") Long id,
            @RequestParam("status") String status  // ← Usa String
    );

    @GetMapping("/api/v1/parking-spaces/{id}")
    ParkingSpaceInformation getParkingSpaceByIdInformation(@PathVariable("id") Long id);



}
