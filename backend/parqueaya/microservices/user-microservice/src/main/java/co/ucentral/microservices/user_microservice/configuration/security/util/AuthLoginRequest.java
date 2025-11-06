package co.ucentral.microservices.user_microservice.configuration.security.util;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;

@Builder
public record AuthLoginRequest(

        @NotBlank
        String username,

        @NotBlank
        String password

) {
}
