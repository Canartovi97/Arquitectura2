package co.ucentral.microservices.user_microservice.exception;

import lombok.EqualsAndHashCode;
import lombok.Getter;

@EqualsAndHashCode(callSuper = true)
@Getter
public class UserNotFoundException extends RuntimeException {

  private final Long userId;

  public UserNotFoundException(Long userId){
    super();
    this.userId = userId;
  }
}
