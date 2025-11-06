package co.ucentral.microservices.user_microservice.domain.user;

import jakarta.validation.constraints.*;
import lombok.Builder;

@Builder
public record UpdateUserRequest(

        @NotNull Long id,
        String username,
        String password,
        String name,
        String lastname,

        @NotBlank(message = "the email is required")
        @Email(message = "the email must be in a valid format")
        String email,

        @Min(value = 15, message = "age must be greater than 15")
        @Max(value = 110, message = "age must be less than 8")
        Integer age,
        Integer phone

) {
}
