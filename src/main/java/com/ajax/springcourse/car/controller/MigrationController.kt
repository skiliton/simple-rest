package com.ajax.springcourse.car.controller

import com.ajax.springcourse.car.migration.CarMigration
import com.ajax.springcourse.car.migration.MigrationResult
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.context.annotation.Profile
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping(path = ["/api/migration"], produces = ["application/json"])
@Profile("migration")
class MigrationController(
    @param:Qualifier("mongoToRedisMigration") private val mongoToRedisMigration: CarMigration,
    @param:Qualifier("redisToMongoMigration") private val redisToMongoMigration: CarMigration
) {
    @GetMapping("/mongoToRedis")
    fun fromMongoToRedis(): MigrationResult = mongoToRedisMigration.migrate()

    @GetMapping("/redisToMongo")
    fun fromRedisToMongo(): MigrationResult = redisToMongoMigration.migrate()
}