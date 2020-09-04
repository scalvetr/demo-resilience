package com.example.demoresilience.rest

import io.github.resilience4j.bulkhead.annotation.Bulkhead
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker
import io.github.resilience4j.retry.annotation.Retry

// TODO check why annotations are not working
interface BackendAClient {
    companion object {
        const val BACKEND_NAME = "backendA"
    }

    //@CircuitBreaker(name = CIRCUIT)
    //@Bulkhead(name = CIRCUIT)
    //@Retry(name = CIRCUIT)
    fun getMessage(): String

    //@CircuitBreaker(name = CIRCUIT)
    //@Bulkhead(name = CIRCUIT)
    //@Retry(name = CIRCUIT)
    fun postMessage(message: String): String


}