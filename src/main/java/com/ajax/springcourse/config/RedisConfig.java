package com.ajax.springcourse.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;
import org.springframework.data.redis.connection.ReactiveRedisConnection;
import org.springframework.data.redis.connection.ReactiveRedisConnectionFactory;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.ReactiveRedisTemplate;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@Configuration
@Profile({"redis", "migration","redis_reactive"})
public class RedisConfig {

    @Value("${spring.redis.host}")
    private String redisHostName;

    @Value("${spring.redis.port}")
    private int redisPort;

    @Bean
    public LettuceConnectionFactory lettuceConnectionFactory() {
        return new LettuceConnectionFactory("localhost", 6379);
    }

    @Bean
    RedisConnection redisConnection(LettuceConnectionFactory factory) {
        return factory.getConnection();
    }

    @Bean
    ReactiveRedisConnection reactiveRedisConnection(LettuceConnectionFactory factory) {
        return factory.getReactiveConnection();
    }

    @Bean
    @Primary
    public ReactiveRedisTemplate<String,String> reactiveRedisTemplate(LettuceConnectionFactory factory){
        return new ReactiveRedisTemplate<>(
                factory,
                RedisSerializationContext
                    .<String, String>newSerializationContext()
                    .key(StringRedisSerializer.UTF_8)
                    .value(StringRedisSerializer.UTF_8)
                    .hashKey(StringRedisSerializer.UTF_8)
                    .hashValue(StringRedisSerializer.UTF_8)
                    .build());
    }

    @Bean
    @Primary
    public RedisTemplate<String, String> redisTemplate(LettuceConnectionFactory factory) {
        final RedisTemplate<String, String> template = new RedisTemplate<>();
        template.setConnectionFactory(factory);
        template.setKeySerializer(StringRedisSerializer.UTF_8);
        template.setValueSerializer(StringRedisSerializer.UTF_8);
        return template;
    }
}
