package co.ucentral.microservices.user_microservice.configuration.mapper;

import co.ucentral.microservices.user_microservice.domain.user.NewUserRequest;
import co.ucentral.microservices.user_microservice.domain.user.UpdateUserRequest;
import co.ucentral.microservices.user_microservice.domain.user.User;
import co.ucentral.microservices.user_microservice.domain.user.UserResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.function.Consumer;

@Component
@RequiredArgsConstructor
public class UserMapping {

    private final PasswordEncoder passwordEncoder;

    public User toUser(NewUserRequest newUserDto){
        return User.builder()
                .username(newUserDto.username())
                .password(passwordEncoder.encode(newUserDto.password()))
                .name(newUserDto.name())
                .lastname(newUserDto.lastname())
                .email(newUserDto.email())
                .age(newUserDto.age())
                .phone(newUserDto.phone())
                .build();
    }

    public UserResponse toUserResponse(User user){
        return UserResponse.builder()
                .id(user.getId())
                .username(user.getUsername())
                .lastname(user.getLastname())
                .name(user.getName())
                .lastname(user.getLastname())
                .email(user.getEmail())
                .age(user.getAge())
                .phone(user.getPhone())
                .build();
    }

    public User updateUserFromDto(UpdateUserRequest dto, User user){
        setIfNotNull(dto.username(),user::setUsername);
        setIfNotNull(dto.password(),user::setPassword);
        setIfNotNull(dto.name(),user::setName);
        setIfNotNull(dto.lastname(),user::setLastname);
        setIfNotNull(dto.email(),user::setEmail);
        setIfNotNull(dto.age(),user::setAge);
        setIfNotNull(dto.phone(),user::setPhone);
        return user;
    }

    private <T> void setIfNotNull(T value, Consumer<T> setter){
        if(value != null){
            setter.accept(value);
        }
    }

}
