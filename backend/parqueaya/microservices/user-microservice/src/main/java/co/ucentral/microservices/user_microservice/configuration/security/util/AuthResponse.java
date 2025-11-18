package co.ucentral.microservices.user_microservice.configuration.security.util;

import lombok.Builder;

@Builder
public record AuthResponse(Long id,String jwt,String message, String role) {
}
