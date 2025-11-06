package co.ucentral.microservices.booking_microservice.controller;

import co.ucentral.microservices.booking_microservice.client.ParkingSpaceInformation;
import co.ucentral.microservices.booking_microservice.domain.booking.Booking;
import co.ucentral.microservices.booking_microservice.domain.booking.BookingResponse;
import co.ucentral.microservices.booking_microservice.domain.booking.CancelBookingRequest;
import co.ucentral.microservices.booking_microservice.domain.booking.CreateBookingRequest;
import co.ucentral.microservices.booking_microservice.service.BookingService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;


@RestController
@RequestMapping("/api/v1/bookings")
@RequiredArgsConstructor
public class BookingController {

    private final BookingService bookingService;

    @GetMapping("/{id}")
    public ResponseEntity<BookingResponse> getBookingById(@PathVariable UUID id) {
        BookingResponse response = bookingService.getBookingById(id);
        return ResponseEntity.ok(response);
    }

    @PostMapping
    public ResponseEntity<BookingResponse> createBooking(@Valid @RequestBody CreateBookingRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(bookingService.createBooking(request));
    }

    @PatchMapping("/{id}/cancel")
    public ResponseEntity<BookingResponse> cancelBooking(
            @PathVariable UUID id,
            @RequestBody(required = false) CancelBookingRequest request) {

        String reason = (request != null) ? request.reason() : null;
        BookingResponse response = bookingService.cancelBooking(id, reason);
        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<Page<BookingResponse>> getBookings(
            @RequestParam(required = false) Long userId,
            @RequestParam(required = false) Long parkingSpaceId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        if (size > 100) size = 100;

        Pageable pageable = PageRequest.of(page, size);
        Page<BookingResponse> response = bookingService.findBookings(userId, parkingSpaceId, pageable);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}/parkingInformation")
    public ResponseEntity<ParkingSpaceInformation> getBookingByIdParking(@PathVariable UUID id) {
        BookingResponse response = bookingService.getBookingById(id);
        return ResponseEntity.ok(bookingService.getBookingByIdParking(response.parkingSpaceId()));
    }



}
