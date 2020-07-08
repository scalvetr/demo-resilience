package com.example.demoresilience.service

import com.example.demoresilience.config.loggerFor
import com.example.demoresilience.rest.ClientBackendA
import com.example.demoresilience.rest.ClientBackendB
import io.github.resilience4j.circuitbreaker.CircuitBreakerRegistry
import org.springframework.stereotype.Service
import java.util.function.Supplier

@Service
class DemoServiceImpl constructor(
        var circuitBreakerRegistry: CircuitBreakerRegistry,
        var backendA: ClientBackendA,
        var backendB: ClientBackendB
) : DemoService {
    var log = loggerFor(this::class.java)
    override fun getBackendA(): String {
        log.info("getBackendA")
        return circuitBreakerRegistry.circuitBreaker(ClientBackendA.CIRCUIT).decorateCheckedSupplier {
            backendA.getMessage()
            /*}.apply().recover { e -> Supplier { handleError(e) } }.get()*/
        }.recover { e -> Supplier { handleError(e) } }.get()
    }

    override fun postBackendA(message: String): String {
        log.info("postBackendA $message")
        return circuitBreakerRegistry.circuitBreaker(ClientBackendA.CIRCUIT).decorateCheckedSupplier {
            backendA.postMessage(message)
        }.recover { e -> Supplier { handleError(e) } }.get();
    }

    override fun getBackendB(): String {
        log.info("getBackendB")
        return circuitBreakerRegistry.circuitBreaker(ClientBackendB.CIRCUIT).decorateCheckedSupplier {
            backendB.getMessage()
        }.recover { e -> Supplier { handleError(e) } }.get();
    }

    override fun postBackendB(message: String): String {
        log.info("postBackendB $message")
        return circuitBreakerRegistry.circuitBreaker(ClientBackendB.CIRCUIT).decorateCheckedSupplier {
            backendB.postMessage(message)
        }.recover { e -> Supplier { handleError(e) } }.get();
    }

    fun handleError(error: Throwable): String {
        return "{\"error\": \"${error.message}\"}"
    }
}