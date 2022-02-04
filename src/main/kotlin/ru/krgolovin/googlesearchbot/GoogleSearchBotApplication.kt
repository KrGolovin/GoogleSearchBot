package ru.krgolovin.googlesearchbot

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class GoogleSearchBotApplication

fun main(args: Array<String>) {
	runApplication<GoogleSearchBotApplication>(*args)
}
