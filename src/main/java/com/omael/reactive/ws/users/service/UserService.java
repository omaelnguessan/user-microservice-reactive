package com.omael.reactive.ws.users.service;

import com.omael.reactive.ws.users.presentation.dto.UserRest;
import com.omael.reactive.ws.users.presentation.request.CreateUserRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.ReactiveUserDetailsService;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.UUID;

public interface UserService extends ReactiveUserDetailsService {
    Mono<UserRest> createUser(Mono<CreateUserRequest> createUserRequest);
    Mono<UserRest> getUserById(UUID userId);
    Flux<UserRest> getAllUsers(Pageable pageable);
    Flux<UserRest> streamUser();
}
