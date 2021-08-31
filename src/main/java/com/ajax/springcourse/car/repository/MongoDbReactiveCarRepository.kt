package com.ajax.springcourse.car.repository

import com.ajax.springcourse.car.model.Car
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Profile
import org.springframework.data.mongodb.core.ReactiveMongoTemplate
import org.springframework.data.mongodb.core.query.Criteria.where
import org.springframework.data.mongodb.core.query.Query.query
import org.springframework.data.mongodb.core.query.where
import org.springframework.stereotype.Component
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import kotlin.reflect.KProperty


@Component
@Profile("mongo_reactive")
class MongoDbReactiveCarRepository @Autowired constructor(private val template: ReactiveMongoTemplate): ReactiveCarRepository {

    companion object{
        const val COLLECTION_NAME = "cars"
    }

    override fun findByModel(model: String): Flux<Car> = template
        .find(
            query(where("model").`is`(model)),
            Car::class.java,
            COLLECTION_NAME)

    override fun findById(id: String): Mono<Car> = template
        .findById(
            id,
            Car::class.java,
            COLLECTION_NAME)

    override fun save(car: Car): Mono<Car> = template.save(car, COLLECTION_NAME)

    override fun findAll(): Flux<Car> = template.findAll(Car::class.java, COLLECTION_NAME)

    override fun deleteAll(): Mono<Unit> = template.dropCollection(COLLECTION_NAME).ofType(Unit::class.java)
}