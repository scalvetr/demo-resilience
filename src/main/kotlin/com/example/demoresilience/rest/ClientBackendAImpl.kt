package com.example.demoresilience.rest

import com.example.demoresilience.config.loggerFor
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.HttpEntity
import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType
import org.springframework.stereotype.Service
import org.springframework.web.client.RestTemplate

@Service
class ClientBackendAImpl constructor(
        @Qualifier("restTemplate") var rest: RestTemplate,
        @Value("\${services.backendA.base-url}") var backendBaseURL: String
) : ClientBackendA {
    var log = loggerFor(this::class.java)

    override fun getMessage(): String {
        log.info("getMessage")
        return rest.getForObject("${backendBaseURL}/message", String::class.java)!!
    }

    override fun postMessage(message: String): String {
        log.info("postMessage $message")
        val headers = HttpHeaders()
        headers.contentType = MediaType.APPLICATION_JSON
        val entity = HttpEntity(message, headers)
        return rest.postForObject("${backendBaseURL}/message", entity, String::class.java)!!
    }
}