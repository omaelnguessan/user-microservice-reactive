package com.omael.reactive.ws.users.presentation.controller;

import com.omael.reactive.ws.users.presentation.dto.UserRest;
import com.omael.reactive.ws.users.presentation.request.CreateUserRequest;
import com.omael.reactive.ws.users.service.UserService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.net.URI;
import java.util.UUID;

@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<ResponseEntity<UserRest>> createUser(@RequestBody @Valid Mono<CreateUserRequest> createUserRequest) {
        return userService.createUser(createUserRequest)
                .map(user -> ResponseEntity
                        .status(HttpStatus.CREATED)
                        .location(URI.create("/users/" + user.getId()))
                        .body(user));
    }

    @GetMapping("/{userId}")
    @PreAuthorize("authentication.principal.equals(#userId.toString()) or hasRole('ROLE_ADMIN')")
    public Mono<ResponseEntity<UserRest>> getUser(@PathVariable("userId") UUID userId) {
        return userService.getUserById(userId)
                .map(userRest -> ResponseEntity
                        .status(HttpStatus.OK)
                        .body(userRest))
                .switchIfEmpty(Mono.just(ResponseEntity.notFound().build()));
    }

    @GetMapping
    public Flux<ResponseEntity<UserRest>> getAllUsers(@PageableDefault Pageable pageable) {
        return userService.getAllUsers(pageable)
                .map(userRest -> ResponseEntity
                        .status(HttpStatus.OK)
                        .body(userRest));
    }
}
