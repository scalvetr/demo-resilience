package com.example.democircuitbreaker

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class DemoCircuitbreakerApplication

fun main(args: Array<String>) {
    runApplication<DemoCircuitbreakerApplication>(*args)
}
