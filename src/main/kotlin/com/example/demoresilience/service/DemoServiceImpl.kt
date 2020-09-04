package com.example.demoresilience.service

import com.example.demoresilience.config.loggerFor
import com.example.demoresilience.rest.BackendAClient
import com.example.demoresilience.rest.BackendBClient
import io.github.resilience4j.bulkhead.BulkheadRegistry
import io.github.resilience4j.circuitbreaker.CircuitBreakerRegistry
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker
import io.github.resilience4j.kotlin.circuitbreaker.decorateFunction
import io.github.resilience4j.timelimiter.TimeLimiterRegistry
import org.springframework.stereotype.Service
import java.util.concurrent.CompletableFuture
import java.util.function.Supplier

@Service
class DemoServiceImpl constructor(
        var circuitBreakerRegistry: CircuitBreakerRegistry,
        var timeLimiterRegistry: TimeLimiterRegistry,
        var bulkheadRegistry: BulkheadRegistry,
        var backendA: BackendAClient,
        var backendB: BackendBClient
) : DemoService {
    var log = loggerFor(this::class.java)

    override fun getBackendA(): String {
        log.info("getBackendA")
        val call = {
            backendA.getMessage()
        }
        val future = Supplier<CompletableFuture<String>> { CompletableFuture.supplyAsync(call) }
        val timeoutDecorated = timeLimiterRegistry.timeLimiter(BackendAClient.BACKEND_NAME).decorateFutureSupplier(future)
        val cbDecorated = circuitBreakerRegistry.circuitBreaker(BackendAClient.BACKEND_NAME).decorateCallable(timeoutDecorated)

        return bulkheadRegistry.bulkhead(BackendAClient.BACKEND_NAME).executeCallable(cbDecorated)
    }

    override fun postBackendA(message: String): String {
        log.info("postBackendA $message")
        return circuitBreakerRegistry.circuitBreaker(BackendAClient.BACKEND_NAME).decorateFunction {
            backendA.postMessage(message)
        }.invoke()
    }

    override fun getBackendB(): String {
        log.info("getBackendB")
        return circuitBreakerRegistry.circuitBreaker(BackendBClient.BACKEND_NAME).decorateFunction {
            backendB.getMessage()
        }.invoke()
    }

    override fun postBackendB(message: String): String {
        log.info("postBackendB $message")
        return circuitBreakerRegistry.circuitBreaker(BackendBClient.BACKEND_NAME).decorateFunction {
            backendB.postMessage(message)
        }.invoke()
    }

}