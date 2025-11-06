package co.ucentral.microservices.payment_microservice.domain.payment;

public enum PaymentStatus {

    PENDING,      // Pago iniciado, esperando confirmación
    COMPLETED,    // Pago exitoso
    FAILED,       // Pago fallido
    REFUNDED      // Reembolso completado


}
