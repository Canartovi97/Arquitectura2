package co.ucentral.microservices.booking_microservice.domain.booking;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Repository
public interface BookingRepository extends JpaRepository<Booking, UUID> {

    /**
     * Verifica si hay reservas activas que se superpongan con el rango de tiempo dado.
     */
    @Query("SELECT b FROM Booking b WHERE " +
            "b.parkingSpaceId = :parkingSpaceId AND " +
            "b.status = :status AND " +
            "(b.startTime < :endTime AND b.endTime > :startTime)")
    List<Booking> findOverlappingBookings(
            @Param("parkingSpaceId") Long parkingSpaceId,
            @Param("startTime") LocalDateTime startTime,
            @Param("endTime") LocalDateTime endTime,
            @Param("status") BookingStatus status
    );


    // Por usuario
    Page<Booking> findByUserId(Long userId, Pageable pageable);

    // Por espacio
    Page<Booking> findByParkingSpaceId(Long parkingSpaceId, Pageable pageable);

    // Por usuario y espacio
    Page<Booking> findByUserIdAndParkingSpaceId(Long userId, Long parkingSpaceId, Pageable pageable);


}
