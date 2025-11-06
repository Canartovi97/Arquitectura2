package co.ucentral.microservices.booking_microservice.domain.booking;

public enum BookingStatus {
    PENDING,      // Reserva creada, esperando confirmación (opcional)
    CONFIRMED,    // Reserva activa
    COMPLETED,    // Reserva finalizada (tiempo terminado)
    CANCELLED     // Reserva cancelada por el usuario o sistema

}
