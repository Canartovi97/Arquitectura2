package co.ucentral.microservices.parking_microservice.domain.parkingImage;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface ParkingImageRepository extends JpaRepository<ParkingImage, UUID> {

    List<ParkingImage> findByParkingSpaceId(Long parkingSpaceId);
}
