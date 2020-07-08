package com.example.demoresilience.rest

import io.github.resilience4j.bulkhead.annotation.Bulkhead
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker
import io.github.resilience4j.retry.annotation.Retry

interface ClientBackendA {
    companion object {
        const val CIRCUIT = "backendA"
    }

    @CircuitBreaker(name = CIRCUIT, fallbackMethod = "fallback")
    @Bulkhead(name = CIRCUIT, fallbackMethod = "fallback")
    @Retry(name = CIRCUIT, fallbackMethod = "fallback")
    fun getMessage(): String

    @CircuitBreaker(name = CIRCUIT, fallbackMethod = "fallback")
    @Bulkhead(name = CIRCUIT, fallbackMethod = "fallback")
    @Retry(name = CIRCUIT, fallbackMethod = "fallback")
    fun postMessage(message: String): String


    fun fallback(e: Throwable): String {
        return "{\"message\", \"${e.message}\"}"
    }
}