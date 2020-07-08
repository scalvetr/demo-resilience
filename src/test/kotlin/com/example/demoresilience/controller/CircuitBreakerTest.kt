package com.example.demoresilience.controller

import com.example.demoresilience.AbstractCircuitBreakerTest
import com.example.demoresilience.rest.ClientBackendA
import com.example.demoresilience.rest.ClientBackendB
import com.ninjasquad.springmockk.MockkBean
import io.github.resilience4j.circuitbreaker.CircuitBreaker
import io.mockk.every
import org.junit.jupiter.api.Disabled
import org.junit.jupiter.api.Test
import org.springframework.http.HttpEntity
import org.springframework.http.HttpMethod
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.client.ResourceAccessException
import strikt.api.expectThat
import strikt.assertions.isEqualTo

class CircuitBreakerTest : AbstractCircuitBreakerTest() {
    @MockkBean
    lateinit var clientBackendA: ClientBackendA

    @MockkBean
    lateinit var clientBackendB: ClientBackendB

    @Test @Disabled
    fun shouldOpenBackendACircuitBreaker() {

        // When
        listOf(1, 2).forEach { _ -> produceFailure(BackendCall.GET_BACKEND_A) }

        // Then
        checkHealthStatus(BackendCall.GET_BACKEND_A.circuit, CircuitBreaker.State.OPEN)
    }

    @Test @Disabled
    fun shouldCloseBackendACircuitBreaker() {
        transitionToOpenState(BackendCall.GET_BACKEND_A.circuit)
        circuitBreakerRegistry.circuitBreaker(BackendCall.GET_BACKEND_A.circuit).transitionToHalfOpenState()

        // When
        listOf(1, 3).forEach { _ -> produceSuccess(BackendCall.GET_BACKEND_A) }

        // Then
        checkHealthStatus(BackendCall.GET_BACKEND_A.circuit, CircuitBreaker.State.CLOSED)
    }

    @Test @Disabled
    fun shouldOpenBackendBCircuitBreaker() {
        // When
        listOf(1, 4).forEach { _ -> produceFailure(BackendCall.GET_BACKEND_B) }

        // Then
        checkHealthStatus(BackendCall.GET_BACKEND_B.circuit, CircuitBreaker.State.OPEN)
    }

    @Test @Disabled
    fun shouldCloseBackendBCircuitBreaker() {
        transitionToOpenState(BackendCall.GET_BACKEND_B.circuit)
        circuitBreakerRegistry.circuitBreaker(BackendCall.GET_BACKEND_B.circuit).transitionToHalfOpenState()

        // When
        listOf(1, 3).forEach { _ -> produceSuccess(BackendCall.GET_BACKEND_B) }

        // Then
        checkHealthStatus(BackendCall.GET_BACKEND_B.circuit, CircuitBreaker.State.CLOSED)
    }

    private fun produceFailure(backendCall: BackendCall) {
        every { clientBackendA.getMessage() } throws ResourceAccessException("I/O error on " + backendCall.circuit)
        every { clientBackendA.postMessage(any()) } throws ResourceAccessException("I/O error on " + backendCall.circuit)
        every { clientBackendB.getMessage() } throws ResourceAccessException("I/O error on " + backendCall.circuit)
        every { clientBackendB.postMessage(any()) } throws ResourceAccessException("I/O error on " + backendCall.circuit)

        val response = performCall(backendCall)
        expectThat(response) {
            get { statusCode }.isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR)
        }
    }

    private fun produceSuccess(backendCall: BackendCall) {
        every { clientBackendA.getMessage() } returns
                "{\"sample\": \"sample\"}"
        every { clientBackendA.postMessage(any()) } returnsArgument 0
        every { clientBackendB.getMessage() } returns
                "{\"sample\": \"sample\"}"
        every { clientBackendB.postMessage(any()) } returnsArgument 0

        val response = performCall(backendCall)
        expectThat(response) {
            get { statusCode }.isEqualTo(HttpStatus.OK)
        }
    }


    private fun performCall(backendCall: BackendCall): ResponseEntity<String> {
        return when (backendCall.method) {
            HttpMethod.GET -> restTemplate.getForEntity("${backendCall.url}", String::class.java)
            else -> restTemplate.postForEntity("${backendCall.url}", HttpEntity("{\"sample\": \"sample\"}"), String::class.java)
        }
    }
}