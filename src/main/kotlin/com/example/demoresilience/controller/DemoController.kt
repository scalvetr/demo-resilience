package com.example.demoresilience.controller

import com.example.demoresilience.service.DemoService
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/v1")
class DemoController constructor(var demoService: DemoService) {

    @GetMapping(
            path = ["/serviceA"],
            produces = [MediaType.APPLICATION_JSON_VALUE])
    fun getServiceA(): String {
        return demoService.getBackendA()
    }

    @PostMapping(
            path = ["/serviceA"],
            produces = [MediaType.APPLICATION_JSON_VALUE],
            consumes = [MediaType.APPLICATION_JSON_VALUE])
    fun postServiceA(@RequestBody message: String): String {
        return demoService.postBackendA(message)
    }


    @GetMapping(
            path = ["/serviceB"],
            produces = [MediaType.APPLICATION_JSON_VALUE])
    fun getServiceB(): String {
        return demoService.getBackendB()
    }

    @PostMapping(
            path = ["/serviceB"],
            produces = [MediaType.APPLICATION_JSON_VALUE],
            consumes = [MediaType.APPLICATION_JSON_VALUE])
    fun postServiceB(@RequestBody message: String): String {
        return demoService.postBackendB(message)
    }

}