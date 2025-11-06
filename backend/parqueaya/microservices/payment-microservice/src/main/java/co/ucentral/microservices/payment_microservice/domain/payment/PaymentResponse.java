package co.ucentral.microservices.payment_microservice.domain.payment;

public record PaymentResponse(
        String paymentUrl,
        String paymentId
) {
}
