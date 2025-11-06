package co.ucentral.microservices.user_microservice.configuration.security.util;

import lombok.Builder;

@Builder
public record AuthResponse(String jwt,String message) {
}
