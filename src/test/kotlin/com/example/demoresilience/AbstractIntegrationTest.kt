package com.example.demoresilience

import com.example.demoresilience.rest.BackendAClient
import com.example.demoresilience.rest.BackendBClient
import io.github.resilience4j.circuitbreaker.CircuitBreaker
import io.github.resilience4j.circuitbreaker.CircuitBreakerRegistry
import org.junit.jupiter.api.BeforeEach
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.http.HttpMethod

@AutoConfigureWebTestClient
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = [DemoResilienceApplication::class])
abstract class AbstractIntegrationTest {
    enum class BackendCall(val circuit: String, val url: String, var method: HttpMethod) {
        GET_BACKEND_A(BackendAClient.BACKEND_NAME, "/serviceA", HttpMethod.GET),
        POST_BACKEND_A(BackendAClient.BACKEND_NAME, "/serviceA", HttpMethod.POST),
        GET_BACKEND_B(BackendBClient.BACKEND_NAME, "/serviceB", HttpMethod.GET),
        POST_BACKEND_B(BackendBClient.BACKEND_NAME, "/serviceB", HttpMethod.POST)
    }

    @Autowired
    lateinit var circuitBreakerRegistry: CircuitBreakerRegistry

    @Autowired
    lateinit var restTemplate: TestRestTemplate

    //@Autowired
    //lateinit var webClient: WebTestClient


    @BeforeEach
    fun setup() {
        transitionToClosedState(BackendCall.GET_BACKEND_A.circuit)
        transitionToClosedState(BackendCall.POST_BACKEND_A.circuit)
        transitionToClosedState(BackendCall.GET_BACKEND_B.circuit)
        transitionToClosedState(BackendCall.POST_BACKEND_B.circuit)
    }

    protected fun transitionToOpenState(circuitBreakerName: String) {
        val circuitBreaker = circuitBreakerRegistry.circuitBreaker(circuitBreakerName)
        if (circuitBreaker.state != CircuitBreaker.State.OPEN)
            circuitBreaker.transitionToOpenState()
    }

    protected fun transitionToClosedState(circuitBreakerName: String) {
        val circuitBreaker = circuitBreakerRegistry.circuitBreaker(circuitBreakerName)
        if (circuitBreaker.state != CircuitBreaker.State.CLOSED)
            circuitBreaker.transitionToClosedState()
    }

}