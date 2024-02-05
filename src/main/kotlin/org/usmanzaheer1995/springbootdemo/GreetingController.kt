package org.usmanzaheer1995.springbootdemo

import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.RestController
import org.usmanzaheer1995.springbootdemo.openapi.api.GreetingApi

@RestController
class GreetingController : GreetingApi {
    override fun getGreeting(): ResponseEntity<String> {
        return ResponseEntity.ok("Hello, World!")
    }
}
