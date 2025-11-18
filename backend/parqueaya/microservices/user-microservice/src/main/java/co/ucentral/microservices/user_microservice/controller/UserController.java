package co.ucentral.microservices.user_microservice.controller;

import co.ucentral.microservices.user_microservice.domain.user.NewUserRequest;
import co.ucentral.microservices.user_microservice.domain.user.UpdateUserRequest;
import co.ucentral.microservices.user_microservice.domain.user.UserResponse;
import co.ucentral.microservices.user_microservice.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping("/create")
    public ResponseEntity<Void> createUser(@Valid @RequestBody NewUserRequest newUser){
        userService.createUser(newUser);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<UserResponse>> findAllUser(){
        return ResponseEntity.ok(userService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserResponse> findUserById(@PathVariable Long id){
        return ResponseEntity.ok(userService.findUserById(id));
    }

    @PutMapping
    public ResponseEntity<UserResponse> updateUser(@Valid @RequestBody UpdateUserRequest user){
        return new ResponseEntity<>(userService.updateUser(user),HttpStatus.ACCEPTED);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUserById(@PathVariable Long id){
        userService.deleteUserById(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }



}
