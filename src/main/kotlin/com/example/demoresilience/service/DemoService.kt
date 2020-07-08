package com.example.demoresilience.service

interface DemoService {
    fun getBackendA(): String
    fun postBackendA(message: String): String
    fun getBackendB(): String
    fun postBackendB(message: String): String
}