package co.ucentral.microservices.user_microservice.controller;

import co.ucentral.microservices.user_microservice.configuration.security.util.AuthLoginRequest;
import co.ucentral.microservices.user_microservice.configuration.security.util.AuthResponse;
import co.ucentral.microservices.user_microservice.service.UserDetailsServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/user/auth")
@RequiredArgsConstructor
public class AuthController {

    private final UserDetailsServiceImpl userDetailsService;

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> auth(@RequestBody AuthLoginRequest authLoginRequest ){
        return ResponseEntity.ok(userDetailsService.login(authLoginRequest));
    }


}
