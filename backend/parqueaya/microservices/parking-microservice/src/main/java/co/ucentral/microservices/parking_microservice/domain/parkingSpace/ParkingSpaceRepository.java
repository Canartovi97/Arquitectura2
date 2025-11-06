package co.ucentral.microservices.parking_microservice.domain.parkingSpace;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ParkingSpaceRepository extends JpaRepository<ParkingSpace,Long> {


    @Query("SELECT DISTINCT p FROM ParkingSpace p " +
            "LEFT JOIN FETCH p.images " +
            "LEFT JOIN FETCH p.location")
    Page<ParkingSpace> findAllWithImages(Pageable pageable);

    @Query("SELECT DISTINCT p FROM ParkingSpace p " +
            "LEFT JOIN FETCH p.images " +
            "LEFT JOIN FETCH p.location " +
            "WHERE p.status = :status")
    Page<ParkingSpace> findByStatus(@Param("status") ParkingSpaceStatus status, Pageable pageable);


}
