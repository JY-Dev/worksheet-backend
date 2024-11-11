package com.jydev.worksheet.config

import io.swagger.v3.oas.models.OpenAPI
import io.swagger.v3.oas.models.info.Info
import io.swagger.v3.oas.models.servers.Server
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class OpenApiConfig {
    @Bean
    fun openAPI(): OpenAPI {
        val info = Info()
            .title("Worksheet API")
            .version("1.0.0")
            .description("Worksheet API")

        return OpenAPI()
            .servers(
                listOf(Server().url(LOCAL_URL).description("Local (개발)"))
            )
            .info(info)
    }

    companion object {
        const val LOCAL_URL: String = "http://localhost:8080"
    }
}
