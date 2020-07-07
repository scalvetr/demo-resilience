package com.example.democircuitbreaker.service

import com.example.democircuitbreaker.config.loggerFor
import org.springframework.beans.factory.annotation.Value
import org.springframework.cloud.client.circuitbreaker.CircuitBreakerFactory
import org.springframework.stereotype.Service
import org.springframework.web.client.RestTemplate

@Service
class DemoServiceImpl constructor(
        var circuitBreakerFactory: CircuitBreakerFactory<*, *>,
        var rest: RestTemplate,
        @Value("\${services.serviceA.base-url}") var serviceABaseURL: String,
        @Value("\${services.serviceB.base-url}") var serviceBBaseURL: String
) : DemoService {
    var log = loggerFor(this::class.java)

    override fun getServiceA(): String {
        log.info("getServiceA")
        return circuitBreakerFactory.create("serviceA").run({
            rest.getForObject("${serviceABaseURL}/message", String::class.java)!!
        }, ::fallback )
    }

    override fun postServiceA(message: String): String {
        log.info("postServiceA $message")
        return circuitBreakerFactory.create("serviceA").run({
            rest.postForObject("${serviceABaseURL}/message", message, String::class.java)!!
        }, ::fallback )
    }

    override fun getServiceB(): String {
        log.info("getServiceB")
        return circuitBreakerFactory.create("serviceB").run({
            rest.getForObject("${serviceBBaseURL}/message", String::class.java)!!
        }, ::fallback )
    }

    override fun postServiceB(message: String): String {
        log.info("postServiceB $message")
        return circuitBreakerFactory.create("serviceA").run({
            rest.postForObject("${serviceBBaseURL}/message", message, String::class.java)!!
        }, ::fallback )
    }

    fun fallback(e: Throwable): String {
        log.warn("Error captured", e)
        return "{error: \"${e.message}\"}"
    }
}