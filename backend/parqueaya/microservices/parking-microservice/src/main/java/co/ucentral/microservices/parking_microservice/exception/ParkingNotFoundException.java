package co.ucentral.microservices.parking_microservice.exception;

import lombok.EqualsAndHashCode;
import lombok.Getter;

@EqualsAndHashCode(callSuper = true)
@Getter
public class ParkingNotFoundException extends RuntimeException {

  private final Long parkingId;

  public ParkingNotFoundException(Long parkingId){
    super();
    this.parkingId = parkingId;
  }
}
