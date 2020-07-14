package com.example.demoresilience.rest

interface BackendBClient {
    companion object {
        const val CIRCUIT = "backendB"
    }

    fun getMessage(): String
    fun postMessage(message: String): String

}