package com.example.demoresilience

import io.github.resilience4j.circuitbreaker.CircuitBreaker
import strikt.api.expectThat
import strikt.assertions.isEqualTo

abstract class AbstractCircuitBreakerTest : AbstractIntegrationTest() {

    protected fun checkHealthStatus(circuitBreakerName: String, state: CircuitBreaker.State) {
        val circuitBreaker = circuitBreakerRegistry.circuitBreaker(circuitBreakerName)
        expectThat(circuitBreaker) {
            get { this.state }.isEqualTo(state)
        }
    }
}