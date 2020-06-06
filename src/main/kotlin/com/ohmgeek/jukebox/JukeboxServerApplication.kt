package com.ohmgeek.jukebox

import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.context.annotation.ComponentScan

@SpringBootApplication
class JukeboxServerApplication

fun main(args: Array<String>) {
	SpringApplication.run(JukeboxServerApplication::class.java, *args)
}
