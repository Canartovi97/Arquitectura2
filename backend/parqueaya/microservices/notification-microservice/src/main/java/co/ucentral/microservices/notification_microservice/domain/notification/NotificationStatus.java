package co.ucentral.microservices.notification_microservice.domain.notification;

public enum NotificationStatus {

    PENDING,  // Esperando ser enviada
    SENT,     // Enviada exitosamente
    FAILED,   // Falló el envío
    READ      // Usuario la ha leído


}
