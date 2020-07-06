package com.example.democircuitbreaker.service

interface DemoService {
    fun getServiceA(): String
    fun postServiceA(message: String): String
    fun getServiceB(): String
    fun postServiceB(message: String): String
}