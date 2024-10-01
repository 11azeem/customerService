package com.hms.customerservice;

import io.micrometer.core.instrument.Gauge;
import io.micrometer.core.instrument.MeterRegistry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AppConfig {

    @Autowired
    private CredentialRepository credentialRepository;

    @Bean
    public Gauge registeredUsers(MeterRegistry registry) {
        return Gauge.builder("registered-users", credentialRepository::count)
                .description("The number of registered users")
                .tag("type", "custom-metric")
                .register(registry);
    }
}
