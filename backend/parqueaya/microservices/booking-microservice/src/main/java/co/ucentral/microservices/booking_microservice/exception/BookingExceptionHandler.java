package co.ucentral.microservices.booking_microservice.exception;

import co.ucentral.microservices.common_exception.ErrorResponse;
import co.ucentral.microservices.common_exception.GlobalExceptionHandler;
import org.springframework.context.annotation.Primary;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
@Primary
public class BookingExceptionHandler extends GlobalExceptionHandler {

    @ExceptionHandler(BookingNotFoundException.class)
    public ResponseEntity<ErrorResponse> handle404(BookingNotFoundException exception){
        var errors = new HashMap<String,String>();
        var fileName = "Booking";
        var message = String.format("Booking with id %s not found", exception.getBookingId());
        errors.put(fileName,message);
        return new ResponseEntity<>(new ErrorResponse(errors), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(BookingTimeValidationException.class)
    public ResponseEntity<ErrorResponse> handleTimeValidation(BookingTimeValidationException ex) {
        var errors = new HashMap<String,String>();
        var fileName = "Time";
        var message = "End time must be after start time";
        errors.put(fileName,message);
        return ResponseEntity.badRequest().body(new ErrorResponse(errors));
    }

    @ExceptionHandler(ParkingSpaceNotAvailableException.class)
    public ResponseEntity<ErrorResponse> handleTimeValidation(ParkingSpaceNotAvailableException ex) {
        var errors = new HashMap<String,String>();
        var fileName = "Parking not available";
        var message = "Parking space is not available for the selected time slot";
        errors.put(fileName,message);
        return ResponseEntity.badRequest().body(new ErrorResponse(errors));
    }

    @ExceptionHandler(BookingCancellationException.class)
    public ResponseEntity<ErrorResponse> handleTimeValidation(BookingCancellationException ex) {
        var errors = new HashMap<String,String>();
        var fileName = "error";
        var message = ex.getMessage();
        errors.put(fileName,message);
        return ResponseEntity.badRequest().body(new ErrorResponse(errors));
    }

    @ExceptionHandler(InvalidBookingQueryException.class)
    public ResponseEntity<ErrorResponse> handleTimeValidation(InvalidBookingQueryException ex) {
        var errors = new HashMap<String,String>();
        var fileName = "error";
        var message = "At least one of 'userId' or 'parkingSpaceId' must be provided";
        errors.put(fileName,message);
        return ResponseEntity.badRequest().body(new ErrorResponse(errors));
    }



}
