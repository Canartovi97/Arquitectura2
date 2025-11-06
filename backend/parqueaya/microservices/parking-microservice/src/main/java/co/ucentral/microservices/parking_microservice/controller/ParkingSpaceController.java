package co.ucentral.microservices.parking_microservice.controller;

import co.ucentral.microservices.parking_microservice.domain.parkingSpace.NewParkingRequest;
import co.ucentral.microservices.parking_microservice.domain.parkingSpace.ParkingSpace;
import co.ucentral.microservices.parking_microservice.domain.parkingSpace.ParkingSpaceResponse;
import co.ucentral.microservices.parking_microservice.domain.parkingSpace.ParkingSpaceStatus;
import co.ucentral.microservices.parking_microservice.service.ParkingSpaceService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/v1/parking-spaces")
@RequiredArgsConstructor
public class ParkingSpaceController {

    private final ParkingSpaceService parkingSpaceService;

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ParkingSpace> createParkingSpace(
            @RequestPart("request") @Valid NewParkingRequest request,
            @RequestPart(value = "images", required = false) List<MultipartFile> images) {

        ParkingSpace created = parkingSpaceService.createParkingSpace(request, images);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ParkingSpaceResponse> findById(@PathVariable Long id){
        return ResponseEntity.ok(parkingSpaceService.findById(id));

    }

    @GetMapping
    public ResponseEntity<Page<ParkingSpaceResponse>> findAllParkingSpaces(Pageable pageable){
        return ResponseEntity.ok(parkingSpaceService.findAllParkingSpaces(pageable));

    }


    @GetMapping("/available")
    public ResponseEntity<Page<ParkingSpaceResponse>> getAvailableParkingSpaces(
            Pageable pageable) {
        Page<ParkingSpaceResponse> response = parkingSpaceService.findAvailableSpaces(pageable);
        return ResponseEntity.ok(response);
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteParkingSpace(@PathVariable Long id) {
        parkingSpaceService.deleteParkingSpace(id);
        return ResponseEntity.noContent().build(); // 204 No Content
    }


    @PutMapping("/{id}/status")
    public ResponseEntity<Void> updateParkingSpaceStatus(
            @PathVariable Long id,
            @RequestParam String status) {  // ← Recibe String

        // Validar y convertir a enum
        ParkingSpaceStatus statusEnum;
        try {
            statusEnum = ParkingSpaceStatus.valueOf(status.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Invalid status: " + status);
        }

        parkingSpaceService.updateStatus(id, statusEnum);
        return ResponseEntity.noContent().build();
    }







}
