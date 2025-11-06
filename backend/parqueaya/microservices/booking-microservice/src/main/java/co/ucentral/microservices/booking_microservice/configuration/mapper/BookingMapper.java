package co.ucentral.microservices.booking_microservice.configuration.mapper;

import co.ucentral.microservices.booking_microservice.domain.booking.Booking;
import co.ucentral.microservices.booking_microservice.domain.booking.BookingResponse;
import co.ucentral.microservices.booking_microservice.domain.booking.BookingStatus;
import co.ucentral.microservices.booking_microservice.domain.booking.CreateBookingRequest;
import org.springframework.stereotype.Component;

@Component
public class BookingMapper {

    public BookingResponse toBookingResponse(Booking booking) {
        if (booking == null) return null;
        return BookingResponse.builder()
                .id(booking.getId())
                .parkingSpaceId(booking.getParkingSpaceId())
                .userId(booking.getUserId())
                .startTime(booking.getStartTime())
                .endTime(booking.getEndTime())
                .totalAmount(booking.getTotalAmount())
                .status(booking.getStatus())
                .createdAt(booking.getCreatedAt())
                .cancelledAt(booking.getCancelledAt())
                .cancellationReason(booking.getCancellationReason())
                .build();
    }

    public Booking toBooking(CreateBookingRequest bookingRequest){
        if (bookingRequest == null) return null;
        return Booking.builder()
                .parkingSpaceId(bookingRequest.parkingSpaceId())
                .userId(bookingRequest.userId())
                .startTime(bookingRequest.startTime())
                .endTime(bookingRequest.endTime())
//                .totalAmount(totalAmount)
                .status(BookingStatus.CONFIRMED)
                .build();
    }


}
