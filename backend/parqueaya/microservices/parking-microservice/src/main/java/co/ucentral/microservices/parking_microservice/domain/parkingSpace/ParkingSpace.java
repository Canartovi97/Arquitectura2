package co.ucentral.microservices.parking_microservice.domain.parkingSpace;

import co.ucentral.microservices.parking_microservice.domain.location.Location;
import co.ucentral.microservices.parking_microservice.domain.parkingImage.ParkingImage;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
@Table(name = "parking_spaces")
@Builder
public class ParkingSpace {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long ownerId; // ID del usuario (del otro microservicio)

    private String title;
    private String description;
    private BigDecimal pricePerHour;

    @Enumerated(EnumType.STRING)
    private ParkingSpaceStatus status;

    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "location_id")
    private Location location;

    @OneToMany(mappedBy = "parkingSpace", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ParkingImage> images = new ArrayList<>();

    private LocalDateTime createdAt;
}
