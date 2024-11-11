package com.jydev.worksheet

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.data.jpa.repository.config.EnableJpaAuditing

@EnableJpaAuditing
@SpringBootApplication
class WorksheetApiApplication

fun main(args: Array<String>) {
    runApplication<WorksheetApiApplication>(*args)
}
