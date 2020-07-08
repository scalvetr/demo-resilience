package com.example.demoresilience.service

import com.example.demoresilience.config.loggerFor
import com.example.demoresilience.rest.ClientBackendA
import com.example.demoresilience.rest.ClientBackendB
import org.springframework.stereotype.Service

@Service
class DemoServiceImpl constructor(
        var backendA: ClientBackendA,
        var backendB: ClientBackendB
) : DemoService {
    var log = loggerFor(this::class.java)
    override fun getBackendA(): String {
        log.info("getBackendA")
        return backendA.getMessage()
    }

    override fun postBackendA(message: String): String {
        log.info("postBackendA $message")
        return backendA.postMessage(message)
    }

    override fun getBackendB(): String {
        log.info("getBackendB")
        return backendB.getMessage()
    }

    override fun postBackendB(message: String): String {
        log.info("postBackendB $message")
        return backendB.postMessage(message)
    }
}