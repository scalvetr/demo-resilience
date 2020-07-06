package com.example.democircuitbreaker.service

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

    override fun getServiceA(): String {
        return circuitBreakerFactory.create("serviceA").run({
            rest.getForObject("${serviceABaseURL}/message", String::class.java)!!
        }, ::fallback )
    }

    override fun postServiceA(message: String): String {
        return circuitBreakerFactory.create("serviceA").run({
            rest.postForObject("${serviceABaseURL}/message", "{message: \"sample message\"}", String::class.java)!!
        }, ::fallback )
    }

    override fun getServiceB(): String {
        return circuitBreakerFactory.create("serviceB").run({
            rest.getForObject("${serviceBBaseURL}/message", String::class.java)!!
        }, ::fallback )
    }

    override fun postServiceB(message: String): String {
        return circuitBreakerFactory.create("serviceA").run({
            rest.postForObject("${serviceBBaseURL}/message", "{message: \"sample message\"}", String::class.java)!!
        }, ::fallback )
    }

    fun fallback(e: Throwable): String {
        return "{error: \"${e.message}\"}"
    }
}