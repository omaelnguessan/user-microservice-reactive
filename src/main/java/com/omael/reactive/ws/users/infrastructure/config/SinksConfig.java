package com.omael.reactive.ws.users.infrastructure.config;

import com.omael.reactive.ws.users.presentation.dto.UserRest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import reactor.core.publisher.Sinks;

@Configuration
public class SinksConfig {

    @Bean
    public Sinks.Many<UserRest> usersSinks() {
        return Sinks.many().multicast().onBackpressureBuffer();
    }
}
