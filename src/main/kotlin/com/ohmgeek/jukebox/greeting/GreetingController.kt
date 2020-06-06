package com.ohmgeek.jukebox.greeting

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import java.util.concurrent.atomic.AtomicLong

data class Greeting(val id: Long, val content: String)

@RestController
class GreetingController {
    val logger: Logger = LoggerFactory.getLogger(GreetingController::class.java)
    val counter = AtomicLong()

    @GetMapping("/greeting")
    fun greeting(@RequestParam(value = "name", defaultValue = "World") name: String): Greeting {
        logger.error("TEST {}, {}, {}", counter.get(), name, this);
        return Greeting(counter.incrementAndGet(), "Hello, $name")
    }

}

