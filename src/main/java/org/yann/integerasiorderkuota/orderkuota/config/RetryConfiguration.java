package org.yann.integerasiorderkuota.orderkuota.config;

import io.github.resilience4j.retry.Retry;
import io.github.resilience4j.retry.RetryConfig;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.ResourceAccessException;

import java.io.IOException;
import java.time.Duration;

@Configuration
public class RetryConfiguration {

    @Bean
    public Retry callbackRetry() {
        RetryConfig config = RetryConfig.custom()
                .maxAttempts(3)
                .waitDuration(Duration.ofSeconds(2))
                .retryExceptions(
                        IOException.class,
                        RuntimeException.class,
                        ResourceAccessException.class,
                        HttpServerErrorException.class
                ).build();
        return Retry.of("callbackRetry", config);
    }
}
