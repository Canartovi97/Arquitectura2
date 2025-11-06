package co.ucentral.microservices.booking_microservice.service;

import co.ucentral.microservices.booking_microservice.client.ParkingServiceClient;
import co.ucentral.microservices.booking_microservice.client.ParkingSpaceDto;
import co.ucentral.microservices.booking_microservice.client.ParkingSpaceInformation;
import co.ucentral.microservices.booking_microservice.configuration.mapper.BookingMapper;
import co.ucentral.microservices.booking_microservice.domain.booking.*;
import co.ucentral.microservices.booking_microservice.exception.*;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class BookingService {

    private final ParkingServiceClient parkingServiceClient;
    private final BookingRepository bookingRepository;
    private final BookingMapper bookingMapper;

    public BookingResponse getBookingById(UUID bookingId) {
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new BookingNotFoundException(bookingId));
        return bookingMapper.toBookingResponse(booking);
    }

    @Transactional
    public BookingResponse createBooking(CreateBookingRequest request) {
        // 1. Validar rango de tiempo
        if (!request.endTime().isAfter(request.startTime())) {
            throw new BookingTimeValidationException();
        }


        // 2. Verificar disponibilidad
        List<Booking> overlapping = bookingRepository.findOverlappingBookings(
                request.parkingSpaceId(),
                request.startTime(),
                request.endTime(),
                BookingStatus.CONFIRMED
        );


        if (!overlapping.isEmpty()) {
            throw new ParkingSpaceNotAvailableException();
        }

        // 3. Obtener precio por hora
        ParkingSpaceDto parkingSpace = parkingServiceClient.getParkingSpaceById(request.parkingSpaceId());

        // 4. Calcular monto total
        Duration duration = Duration.between(request.startTime(), request.endTime());
        long hours = duration.toHours();
        if (duration.toMinutes() % 60 > 0) {
            hours += 1;
        }
        BigDecimal totalAmount = parkingSpace.pricePerHour().multiply(BigDecimal.valueOf(hours));

        // 5. Crear y guardar reserva
        Booking booking = bookingMapper.toBooking(request);
        booking.setTotalAmount(totalAmount);
        Booking savedBooking = bookingRepository.save(booking);

        // 6. Notificar a parking
        parkingServiceClient.updateParkingSpaceStatus(
                request.parkingSpaceId(),
                "UNAVAILABLE"
        );

        return bookingMapper.toBookingResponse(savedBooking);
    }


    @Transactional
    public BookingResponse cancelBooking(UUID bookingId, String cancellationReason) {
        // 1. Buscar la reserva
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new BookingNotFoundException(bookingId));

        // 2. Validar que se pueda cancelar
        if (booking.getStatus() == BookingStatus.CANCELLED) {
            throw new BookingCancellationException("Booking is already cancelled");
        }

        if (booking.getStatus() == BookingStatus.COMPLETED) {
            throw new IllegalStateException("Cannot cancel a completed booking");
        }

        // Opcional: solo permitir cancelar antes de que empiece
//        if (!booking.getStartTime().isAfter(LocalDateTime.now())) {
//            throw new IllegalStateException("Cannot cancel a booking that has already started");
//        }

        // 3. Actualizar la reserva
        booking.setStatus(BookingStatus.CANCELLED);
        booking.setCancelledAt(LocalDateTime.now());
        booking.setCancellationReason(cancellationReason);
        Booking savedBooking = bookingRepository.save(booking);

        // 4. Notificar a parking para liberar el espacio
        try {
            parkingServiceClient.updateParkingSpaceStatus(
                    booking.getParkingSpaceId(),
                    "AVAILABLE"
            );
        } catch (Exception e) {
            // Opcional: loggear el error, pero no revertir la cancelación
            System.err.println("Warning: Could not update parking space status to AVAILABLE: " + e.getMessage());
            // En producción, podrías usar un mecanismo de reintento (ej: cola de mensajes)
        }

        return bookingMapper.toBookingResponse(savedBooking);

    }


    public Page<BookingResponse> findBookings(
            Long userId,
            Long parkingSpaceId,
            Pageable pageable) {

        Page<Booking> bookings;

        if (userId != null && parkingSpaceId != null) {
            bookings = bookingRepository.findByUserIdAndParkingSpaceId(userId, parkingSpaceId, pageable);
        } else if (userId != null) {
            bookings = bookingRepository.findByUserId(userId, pageable);
        } else if (parkingSpaceId != null) {
            bookings = bookingRepository.findByParkingSpaceId(parkingSpaceId, pageable);
        } else {
            throw new InvalidBookingQueryException();
        }

        return bookings.map(bookingMapper::toBookingResponse);
    }


    public ParkingSpaceInformation getBookingByIdParking(Long id) {
//        Booking booking = bookingRepository.findById(id).orElseThrow();
//        Long spaceId =  booking.getParkingSpaceId();
        return parkingServiceClient.getParkingSpaceByIdInformation(id);
    }
}
