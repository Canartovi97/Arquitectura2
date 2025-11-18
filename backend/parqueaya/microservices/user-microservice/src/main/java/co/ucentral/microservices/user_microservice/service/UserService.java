package co.ucentral.microservices.user_microservice.service;

import co.ucentral.microservices.user_microservice.configuration.mapper.UserMapping;
import co.ucentral.microservices.user_microservice.domain.user.*;
import co.ucentral.microservices.user_microservice.exception.EmailAlreadyExistException;
import co.ucentral.microservices.user_microservice.exception.UserNotFoundException;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final UserMapping userMapping;

    public void createUser(@Valid NewUserRequest newUser) {
        if(userRepository.existsByEmail(newUser.email())){
            throw new EmailAlreadyExistException(newUser.email());
        }
        User user = userMapping.toUser(newUser);
        System.out.println(user.toString());
        userRepository.save(user);
    }

    public List<UserResponse> findAll() {
        return userRepository.findAll().stream().map(userMapping::toUserResponse).toList();
    }

    public UserResponse findUserById(Long id) {
        User user = userRepository.findById(id).orElseThrow(
                ()-> new UserNotFoundException(id));
        return userMapping.toUserResponse(user);
    }

    @Transactional
    public UserResponse updateUser(@Valid UpdateUserRequest userDto) {
        User user = userRepository.findById(userDto.id())
                .orElseThrow(()-> new UserNotFoundException(userDto.id()));
        if(userDto.email() != null && userRepository.existsByEmail(userDto.email())){
            throw new EmailAlreadyExistException(userDto.email());
        }
        User userUpdate = userMapping.updateUserFromDto(userDto,user);
        userRepository.save(userUpdate);
        return userMapping.toUserResponse(userUpdate);
    }

    public void deleteUserById(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(()-> new UserNotFoundException(id));
        userRepository.delete(user);
    }
}
