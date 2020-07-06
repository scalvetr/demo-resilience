package com.example.democircuitbreaker.controller

import com.example.democircuitbreaker.service.DemoService
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/v1")
class DemoController constructor(var demoService: DemoService) {

    @GetMapping(
            path = ["/serviceA"],
            produces = [MediaType.APPLICATION_JSON_VALUE])
    fun getServiceA(): String {
        return demoService.getServiceA()
    }

    @PostMapping(
            path = ["/serviceA"],
            produces = [MediaType.APPLICATION_JSON_VALUE])
    fun postServiceA(message: String): String {
        return demoService.postServiceA(message)
    }


    @GetMapping(
            path = ["/serviceB"],
            produces = [MediaType.APPLICATION_JSON_VALUE])
    fun getServiceB(): String {
        return demoService.getServiceB()
    }

    @PostMapping(
            path = ["/serviceB"],
            produces = [MediaType.APPLICATION_JSON_VALUE])
    fun postServiceB(message: String): String {
        return demoService.postServiceB(message)
    }

}