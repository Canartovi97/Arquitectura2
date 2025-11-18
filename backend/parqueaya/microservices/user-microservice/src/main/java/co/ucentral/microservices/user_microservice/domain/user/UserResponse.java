package co.ucentral.microservices.user_microservice.domain.user;

import lombok.Builder;

@Builder
public record UserResponse(

        Long id,
        String username,
        String name,
        String lastname,
        String email,
        Integer age,
        String phone

) {
}
