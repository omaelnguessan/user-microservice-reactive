package com.omael.reactive.ws.users.service.impl;

import com.omael.reactive.ws.users.data.entity.UserEntity;
import com.omael.reactive.ws.users.data.repository.UserRepository;
import com.omael.reactive.ws.users.service.AuthenticationService;
import com.omael.reactive.ws.users.service.JwtService;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.HashMap;
import java.util.Map;

@Service
public class AuthenticationServiceImpl implements AuthenticationService {

    private final ReactiveAuthenticationManager reactiveAuthenticationManager;
    private final UserRepository userRepository;
    private final JwtService jwtService;

    public AuthenticationServiceImpl(ReactiveAuthenticationManager reactiveAuthenticationManager, UserRepository userRepository, JwtService jwtService) {
        this.reactiveAuthenticationManager = reactiveAuthenticationManager;
        this.userRepository = userRepository;
        this.jwtService = jwtService;
    }

    @Override
    public Mono<Map<String, String>> authenticate(String username, String password) {
        return reactiveAuthenticationManager
                .authenticate(new UsernamePasswordAuthenticationToken(username, password))
                .then(findUserByUsername(username))
                .map(this::createAuthResponse);
    }

    private Mono<UserEntity> findUserByUsername(String username) {
        return userRepository.findByEmail(username);
    }

    private Map<String, String> createAuthResponse(UserEntity userEntity) {
        Map<String, String> authResponse = new HashMap<>();
        authResponse.put("userId", userEntity.getId().toString());
        authResponse.put("token", jwtService.generateJwt(userEntity.getEmail()));
        return authResponse;
    }
}
