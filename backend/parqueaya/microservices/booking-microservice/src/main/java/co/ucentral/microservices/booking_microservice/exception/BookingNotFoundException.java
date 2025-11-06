package co.ucentral.microservices.booking_microservice.exception;

import lombok.EqualsAndHashCode;
import lombok.Getter;

import java.util.UUID;

@EqualsAndHashCode(callSuper = true)
@Getter
public class BookingNotFoundException extends RuntimeException {

    private final UUID bookingId;

    public BookingNotFoundException(UUID bookingId){
        super();
        this.bookingId = bookingId;
    }
}
