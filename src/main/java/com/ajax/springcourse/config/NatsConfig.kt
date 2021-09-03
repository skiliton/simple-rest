package com.ajax.springcourse.config

import io.nats.client.Connection
import io.nats.client.Nats
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class NatsConfig {

    @Bean(destroyMethod = "close")
    fun natsConnection(): Connection = Nats.connect()
}