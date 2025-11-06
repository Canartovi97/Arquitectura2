package co.ucentral.microservices.booking_microservice.exception;

import lombok.EqualsAndHashCode;
import lombok.Getter;

@EqualsAndHashCode(callSuper = true)
@Getter
public class BookingCancellationException extends RuntimeException {

    public BookingCancellationException(String message) {
        super(message);
    }
}
