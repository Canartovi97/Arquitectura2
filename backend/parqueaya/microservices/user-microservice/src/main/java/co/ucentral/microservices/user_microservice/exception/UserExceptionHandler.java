package co.ucentral.microservices.user_microservice.exception;

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
public class UserExceptionHandler extends GlobalExceptionHandler {

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<ErrorResponse> handle404(UserNotFoundException exception){
        var errors = new HashMap<String,String>();
        var fileName = "user";
        var message = String.format("user with id %s not found", exception.getUserId());
        errors.put(fileName,message);
        return new ResponseEntity<>(new ErrorResponse(errors), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(EmailAlreadyExistException.class)
    public ResponseEntity<ErrorResponse> handleDuplicateEmail(EmailAlreadyExistException exception){
        var errors = new HashMap<String,String>();
        var fileName = "email";
        var message = String.format("the email %s is already registered", exception.getEmail());
        errors.put(fileName,message);
        return new ResponseEntity<>(new ErrorResponse(errors),HttpStatus.CONFLICT);
    }
}
