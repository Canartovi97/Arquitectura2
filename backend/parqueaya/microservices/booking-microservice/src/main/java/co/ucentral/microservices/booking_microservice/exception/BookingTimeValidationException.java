package co.ucentral.microservices.booking_microservice.exception;

import lombok.EqualsAndHashCode;
import lombok.Getter;

@EqualsAndHashCode(callSuper = true)
@Getter
public class BookingTimeValidationException extends RuntimeException {


  public BookingTimeValidationException() {
        super();

    }
}
