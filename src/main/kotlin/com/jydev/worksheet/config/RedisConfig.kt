package com.jydev.worksheet.config

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.jsontype.impl.LaissezFaireSubTypeValidator
import com.jydev.worksheet.infra.problem.ProblemCacheHandler
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.data.redis.connection.RedisConnectionFactory
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory
import org.springframework.data.redis.core.RedisTemplate
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer
import org.springframework.data.redis.serializer.StringRedisSerializer

@Configuration
class RedisConfig(
    @Value("\${spring.data.redis.host}")
    private val host: String,

    @Value("\${spring.data.redis.port}")
    private val port: Int,

    private val objectMapper: ObjectMapper
) {

    @Bean
    fun redisConnectionFactory(): RedisConnectionFactory {
        return LettuceConnectionFactory(host, port)
    }

    @Bean
    fun redisProblemTemplate(): RedisTemplate<String, ProblemCacheHandler.ProblemCacheModel> =
        RedisTemplate<String, ProblemCacheHandler.ProblemCacheModel>().apply {
            connectionFactory = redisConnectionFactory()
            keySerializer = StringRedisSerializer()
            valueSerializer = GenericJackson2JsonRedisSerializer(objectMapper.copy().apply {
                val subtypeValidator = LaissezFaireSubTypeValidator.instance
                this.activateDefaultTyping(
                    subtypeValidator,
                    ObjectMapper.DefaultTyping.NON_FINAL
                )
            })
        }
}