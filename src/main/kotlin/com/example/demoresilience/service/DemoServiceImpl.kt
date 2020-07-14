package com.example.demoresilience.service

import com.example.demoresilience.config.loggerFor
import com.example.demoresilience.rest.BackendAClient
import com.example.demoresilience.rest.BackendBClient
import io.github.resilience4j.circuitbreaker.CircuitBreakerRegistry
import io.github.resilience4j.kotlin.circuitbreaker.decorateFunction
import org.springframework.stereotype.Service

@Service
class DemoServiceImpl constructor(
        var circuitBreakerRegistry: CircuitBreakerRegistry,
        var backendA: BackendAClient,
        var backendB: BackendBClient
) : DemoService {
    var log = loggerFor(this::class.java)
    override fun getBackendA(): String {
        log.info("getBackendA")
        return circuitBreakerRegistry.circuitBreaker(BackendAClient.CIRCUIT).decorateFunction {
            backendA.getMessage()
        }.invoke()
    }

    override fun postBackendA(message: String): String {
        log.info("postBackendA $message")
        return circuitBreakerRegistry.circuitBreaker(BackendAClient.CIRCUIT).decorateFunction {
            backendA.postMessage(message)
        }.invoke()
    }

    override fun getBackendB(): String {
        log.info("getBackendB")
        return circuitBreakerRegistry.circuitBreaker(BackendBClient.CIRCUIT).decorateFunction {
            backendB.getMessage()
        }.invoke()
    }

    override fun postBackendB(message: String): String {
        log.info("postBackendB $message")
        return circuitBreakerRegistry.circuitBreaker(BackendBClient.CIRCUIT).decorateFunction {
            backendB.postMessage(message)
        }.invoke()
    }

}