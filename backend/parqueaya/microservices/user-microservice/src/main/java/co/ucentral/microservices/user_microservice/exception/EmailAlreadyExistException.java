package co.ucentral.microservices.user_microservice.exception;

import lombok.EqualsAndHashCode;
import lombok.Getter;

@EqualsAndHashCode(callSuper = true)
@Getter
public class EmailAlreadyExistException extends RuntimeException {

    private final String email;

    public EmailAlreadyExistException(String email) {
        super();
        this.email = email;
    }

}
