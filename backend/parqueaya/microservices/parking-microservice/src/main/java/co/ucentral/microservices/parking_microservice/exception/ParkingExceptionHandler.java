package co.ucentral.microservices.parking_microservice.exception;

import co.ucentral.microservices.common_exception.ErrorResponse;
import co.ucentral.microservices.common_exception.GlobalExceptionHandler;
import org.springframework.context.annotation.Primary;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;

@RestControllerAdvice
@Primary
public class ParkingExceptionHandler extends GlobalExceptionHandler {

    @ExceptionHandler(ParkingNotFoundException.class)
    public ResponseEntity<ErrorResponse> handle404(ParkingNotFoundException exception){
        var errors = new HashMap<String,String>();
        var fileName = "ParkingSpace";
        var message = String.format("ParkingSpace with id %s not found", exception.getParkingId());
        errors.put(fileName,message);
        return new ResponseEntity<>(new ErrorResponse(errors), HttpStatus.NOT_FOUND);
    }

}
