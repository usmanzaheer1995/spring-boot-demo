package org.usmanzaheer1995.springbootdemo

import org.jooq.DSLContext
import org.jooq.generated.persistence.db.tables.references.USERS
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.RestController
import org.usmanzaheer1995.springbootdemo.openapi.api.GreetingApi

@RestController
class GreetingController(
    private val dslContext: DSLContext
) : GreetingApi {
    override fun getGreeting(): ResponseEntity<String> {
        val users = dslContext.selectFrom(USERS).fetch()
        println("users: ${users.size}")
        return ResponseEntity.ok("Hello, World!")
    }
}
