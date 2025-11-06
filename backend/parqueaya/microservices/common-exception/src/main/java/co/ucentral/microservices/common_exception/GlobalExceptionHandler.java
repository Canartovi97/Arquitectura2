package co.ucentral.microservices.common_exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;

@Component
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleException(MethodArgumentNotValidException exception){
        var errors = new HashMap<String,String>();
        exception.getBindingResult().getFieldErrors().forEach(error -> {
            var fileName = error.getField();
            var errorMessage = error.getDefaultMessage();
            errors.put(fileName,errorMessage);
        });
        return new ResponseEntity<>(new ErrorResponse(errors), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleException(Exception exception){
        var errors = new HashMap<String,String>();
        var fileName = "message";
        var errorMessage = "An error has occurred. Please try again later  " + exception;
        errors.put(fileName,errorMessage);
        return new ResponseEntity<>(new ErrorResponse(errors), HttpStatus.INTERNAL_SERVER_ERROR);
    }

}
