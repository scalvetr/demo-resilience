package com.example.demoresilience.service

import com.example.demoresilience.config.loggerFor
import org.springframework.beans.factory.annotation.Value
import org.springframework.cloud.client.circuitbreaker.CircuitBreakerFactory
import org.springframework.http.HttpEntity
import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType
import org.springframework.stereotype.Service
import org.springframework.web.client.RestTemplate

@Service
class DemoServiceImpl constructor(
        var circuitBreakerFactory: CircuitBreakerFactory<*, *>,
        var rest: RestTemplate,
        @Value("\${services.backendA.base-url}") var backendABaseURL: String,
        @Value("\${services.backendB.base-url}") var backendBBaseURL: String
) : DemoService {
    var log = loggerFor(this::class.java)

    override fun getBackendA(): String {
        log.info("getBackendA")
        return circuitBreakerFactory.create("backendA").run({
            rest.getForObject("${backendABaseURL}/message", String::class.java)!!
        }, ::fallback )
    }

    override fun postBackendA(message: String): String {
        log.info("postBackendA $message")
        return circuitBreakerFactory.create("backendA").run({
            val headers = HttpHeaders()
            headers.contentType = MediaType.APPLICATION_JSON
            val entity = HttpEntity(message, headers)
            rest.postForObject("${backendABaseURL}/message", entity, String::class.java)!!
        }, ::fallback )
    }

    override fun getBackendB(): String {
        log.info("getBackendB")
        return circuitBreakerFactory.create("backendB").run({
            rest.getForObject("${backendBBaseURL}/message", String::class.java)!!
        }, ::fallback )
    }

    override fun postBackendB(message: String): String {
        log.info("postBackendB $message")
        return circuitBreakerFactory.create("backendB").run({
            val headers = HttpHeaders()
            headers.contentType = MediaType.APPLICATION_JSON
            val entity = HttpEntity(message, headers)
            rest.postForObject("${backendBBaseURL}/message", entity, String::class.java)!!
        }, ::fallback )
    }

    fun fallback(e: Throwable): String {
        log.warn("Error captured", e)
        return "{ \"error\": \"${e.message}\"}"
    }
}