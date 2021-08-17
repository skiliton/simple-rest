package com.ajax.springcourse.config;

import com.ajax.springcourse.car.migration.CarMigration;
import com.ajax.springcourse.car.migration.CarMigrationImpl;
import com.ajax.springcourse.car.repository.MongoDbCarRepository;
import com.ajax.springcourse.car.repository.RedisCarRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
@Profile("migration")
public class MigrationConfig {

    @Bean
    CarMigration redisToMongoMigration(RedisCarRepository redisCarRepository, MongoDbCarRepository mongoDbCarRepository) {
        return new CarMigrationImpl(redisCarRepository, mongoDbCarRepository);
    }

    @Bean
    CarMigration mongoToRedisMigration(RedisCarRepository redisCarRepository, MongoDbCarRepository mongoDbCarRepository) {
        return new CarMigrationImpl(mongoDbCarRepository, redisCarRepository);
    }
}
