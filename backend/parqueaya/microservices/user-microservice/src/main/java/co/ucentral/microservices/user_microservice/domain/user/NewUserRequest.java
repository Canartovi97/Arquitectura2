package co.ucentral.microservices.user_microservice.domain.user;

import jakarta.validation.constraints.*;
import lombok.Builder;

@Builder
public record NewUserRequest(
        @NotBlank(message = "the username is required")
        String username,

        @NotBlank(message = "the password is requited")
        String password,

        @NotBlank(message = "the name is required")
        String name,

        @NotBlank(message = "the lastname is required")
        String lastname,

        @NotBlank(message = "the email is required")
        @Email(message = "the email must be in a valid format")
        String email,

        @NotNull(message = "the age is required")
        @Min(value = 8, message = "age must be greater than 8")
        @Max(value = 100, message = "age must be less than 8")
        Integer age,

        Integer phone
) {
}
