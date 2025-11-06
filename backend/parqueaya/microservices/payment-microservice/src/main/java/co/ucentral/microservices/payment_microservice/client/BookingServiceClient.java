package co.ucentral.microservices.payment_microservice.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.UUID;

@FeignClient(name = "booking-microservice")
public interface BookingServiceClient {

    @GetMapping("/api/v1/bookings/{id}")
    BookingDto getBookingById(@PathVariable("id") UUID bookingId);

    @GetMapping("/api/v1/bookings/{id}/parkingInformation")
    ParkingSpaceInformation getBookingByIdInformation(@PathVariable("id") UUID bookingId);

}
