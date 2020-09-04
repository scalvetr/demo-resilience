package com.example.demoresilience.rest

interface BackendBClient {
    companion object {
        const val BACKEND_NAME = "backendB"
    }

    fun getMessage(): String
    fun postMessage(message: String): String

}