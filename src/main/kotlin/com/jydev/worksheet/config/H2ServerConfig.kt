package com.jydev.worksheet.config

import org.h2.tools.Server
import org.springframework.boot.CommandLineRunner
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class H2ServerConfig {

    @Bean
    fun startH2TcpServer(): CommandLineRunner {
        return CommandLineRunner {
            Server.createTcpServer(
                "-tcp", "-tcpAllowOthers", "-tcpPort", "9092"
            ).start()
        }
    }
}