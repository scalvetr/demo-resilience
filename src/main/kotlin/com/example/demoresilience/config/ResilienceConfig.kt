package com.example.demoresilience.config

import io.github.resilience4j.circuitbreaker.CircuitBreakerConfig
import io.github.resilience4j.timelimiter.TimeLimiterConfig
import org.springframework.boot.web.client.RestTemplateBuilder
import org.springframework.cloud.circuitbreaker.resilience4j.Resilience4JCircuitBreakerFactory
import org.springframework.cloud.circuitbreaker.resilience4j.Resilience4JConfigBuilder
import org.springframework.cloud.client.circuitbreaker.Customizer
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.client.RestTemplate
import java.time.Duration
import java.util.function.Consumer


@Configuration
class ResilienceConfig {
    /**
     * Default configuration
     */
    @Bean
    fun defaultCustomizer(): Customizer<Resilience4JCircuitBreakerFactory> {
        return Customizer { factory ->
            factory.configureDefault { id ->
                Resilience4JConfigBuilder(id)
                        .circuitBreakerConfig(globalCircuitBreakerConfig())
                        .timeLimiterConfig(globalTimeLimiterConfig()).build()
            }
        }
    }

    /**
     * serviceB configuration
     */
    @Bean
    fun backendBCustomizer(): Customizer<Resilience4JCircuitBreakerFactory> {
        return Customizer { factory: Resilience4JCircuitBreakerFactory ->
            factory.configure(
                    Consumer { builder: Resilience4JConfigBuilder ->
                        builder
                                .circuitBreakerConfig(globalCircuitBreakerConfig())
                                //only one second timeout dor service B
                                .timeLimiterConfig(TimeLimiterConfig.custom().timeoutDuration(Duration.ofSeconds(1)).build()).build()
                    },
                    "backendB")
        }
    }


    @Bean
    fun restTemplate(builder: RestTemplateBuilder): RestTemplate {
        return builder.build()
    }

    fun globalCircuitBreakerConfig(): CircuitBreakerConfig {
        return CircuitBreakerConfig.custom()
                .failureRateThreshold(50f)
                .waitDurationInOpenState(Duration.ofMillis(1000))
                .slidingWindowSize(2).build()
    }

    fun globalTimeLimiterConfig(): TimeLimiterConfig {
        return TimeLimiterConfig.custom()
                .timeoutDuration(Duration.ofSeconds(4)).build();
    }
}