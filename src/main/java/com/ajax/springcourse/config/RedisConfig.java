package com.ajax.springcourse.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
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
@Profile({"redis", "migration"})
public class RedisConfig {

    @Value("${spring.redis.host}")
    private String redisHostName;

    @Value("${spring.redis.port}")
    private int redisPort;

    @Bean
    JedisConnectionFactory jedisConnectionFactory() {
        //RedisStandaloneConfiguration config = new RedisStandaloneConfiguration(redisHostName, redisPort);
        return new JedisConnectionFactory();
    }

    @Bean
    public ReactiveRedisConnectionFactory reactiveRedisConnectionFactory() {
        return new LettuceConnectionFactory("localhost", 6379);
    }

    @Bean
    RedisConnection redisConnection() {
        return jedisConnectionFactory().getConnection();
    }

    @Bean
    ReactiveRedisConnection reactiveRedisConnection() {
        return  reactiveRedisConnectionFactory().getReactiveConnection();
    }

    @Bean
    public ReactiveRedisTemplate<String,String> reactiveRedisTemplate(){
        return new ReactiveRedisTemplate<>(
                reactiveRedisConnectionFactory(),
                RedisSerializationContext
                    .<String, String>newSerializationContext()
                    .key(StringRedisSerializer.UTF_8)
                    .value(StringRedisSerializer.UTF_8)
                    .build());
    }

    @Bean
    public RedisTemplate<String, String> redisTemplate() {
        final RedisTemplate<String, String> template = new RedisTemplate<>();
        template.setConnectionFactory(jedisConnectionFactory());
        template.setKeySerializer(new StringRedisSerializer());
        template.setValueSerializer(new StringRedisSerializer());
        return template;
    }
}
