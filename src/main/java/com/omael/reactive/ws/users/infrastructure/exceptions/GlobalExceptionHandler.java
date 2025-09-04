package com.omael.reactive.ws.users.infrastructure.exceptions;

import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authorization.AuthorizationDeniedException;
import org.springframework.web.ErrorResponse;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.bind.support.WebExchangeBindException;
import reactor.core.publisher.Mono;

import java.util.stream.Collectors;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(DuplicateKeyException.class)
    public Mono<ErrorResponse> handlerDuplicateKeyException(DuplicateKeyException exception) {
        return Mono.just(ErrorResponse.builder(exception, HttpStatus.CONFLICT, exception.getMessage()).build());
    }

    @ExceptionHandler(Exception.class)
    public Mono<ErrorResponse> handlerGeneralException(Exception exception) {
        return Mono.just(ErrorResponse.builder(exception, HttpStatus.INTERNAL_SERVER_ERROR, exception.getMessage()).build());
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public Mono<ErrorResponse> handlerBadRequestException(DataIntegrityViolationException exception) {
        return Mono.just(ErrorResponse.builder(exception, HttpStatus.BAD_REQUEST, exception.getMessage()).build());
    }

    @ExceptionHandler(WebExchangeBindException.class)
    public Mono<ErrorResponse> handlerWebExchangeBindException(WebExchangeBindException exception) {
        String errorMessage = exception.getBindingResult()
                .getAllErrors()
                .stream()
                .map(DefaultMessageSourceResolvable::getDefaultMessage)
                .collect(Collectors.joining(", "));

        return Mono.just(ErrorResponse.builder(exception, HttpStatus.BAD_REQUEST, errorMessage).build());
    }

    @ExceptionHandler(BadCredentialsException.class)
    public Mono<ErrorResponse> handlerBadCredentialsException(BadCredentialsException exception) {
        return Mono.just(ErrorResponse.builder(exception, HttpStatus.UNAUTHORIZED, exception.getMessage()).build());
    }

    @ExceptionHandler(AuthorizationDeniedException.class)
    public Mono<ErrorResponse> handlerAuthorizationDeniedException(AuthorizationDeniedException exception) {
        return Mono.just(ErrorResponse.builder(exception, HttpStatus.FORBIDDEN, exception.getMessage()).build());
    }
}
