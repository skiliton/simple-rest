package com.ajax.springcourse.car.controller;

import com.ajax.springcourse.car.migration.CarMigration;
import com.ajax.springcourse.car.model.Car;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Profile;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(
        path = "/api/migration",
        produces = "application/json"
)
@Profile("migration")
public class MigrationController {

    private final CarMigration mongoToRedisMigration;
    private final CarMigration redisToMongoMigration;

    public MigrationController(@Qualifier("mongoToRedisMigration") CarMigration mongoToRedisMigration,
                               @Qualifier("redisToMongoMigration") CarMigration redisToMongoMigration) {
        this.mongoToRedisMigration = mongoToRedisMigration;
        this.redisToMongoMigration = redisToMongoMigration;
    }

    @GetMapping("/mongoToRedis")
    List<Car> fromMongoToRedis() {
        return mongoToRedisMigration.migrate();
    }

    @GetMapping("/redisToMongo")
    List<Car> fromRedisToMongo() {
        return redisToMongoMigration.migrate();
    }
}
