package co.ucentral.microservices.parking_microservice.domain.parkingImage;

import co.ucentral.microservices.parking_microservice.domain.parkingSpace.ParkingSpace;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
@Table(name = "parking_images")
@Builder
public class ParkingImage {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false)
    private String fileName;      // Ej: "a1b2c3d4.jpg"
    private String originalName;  // Ej: "garaje_frente.jpg"
    private String contentType;   // Ej: "image/jpeg"
    private long size;            // Tamaño en bytes

    // Relación con ParkingSpace
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parking_space_id", nullable = false)
    private ParkingSpace parkingSpace;

    private LocalDateTime uploadedAt;

}
