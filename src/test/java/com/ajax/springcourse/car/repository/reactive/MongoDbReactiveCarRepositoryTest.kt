package com.ajax.springcourse.car.repository.reactive

import com.ajax.springcourse.car.CarFixture
import com.ajax.springcourse.car.model.Car
import com.ajax.springcourse.car.model.dto.CarReadDto
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.Mockito
import org.springframework.data.mongodb.core.ReactiveMongoTemplate
import org.springframework.data.mongodb.core.query.Criteria
import org.springframework.data.mongodb.core.query.Query
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import reactor.test.StepVerifier

class MongoDbReactiveCarRepositoryTest {

    lateinit var template: ReactiveMongoTemplate
    lateinit var repo: MongoDbReactiveCarRepository
    private val fixture = CarFixture()

    @BeforeEach
    fun beforeEach() {
        template = Mockito.mock(ReactiveMongoTemplate::class.java)
        repo = MongoDbReactiveCarRepository(template)
    }

    @Test
    fun shouldDelegateFindByModelCallToMongo() {
        //GIVEN
        val carsWithSameModel = fixture.generateCarsWithSameModel()
        val model = carsWithSameModel.first().model
        Mockito
            .`when`(template.find(
                    Query.query(Criteria.where("model").`is`(model)),
                    Car::class.java,
                    MongoDbReactiveCarRepository.COLLECTION_NAME))
            .thenReturn(
                Flux
                    .just(*carsWithSameModel.toTypedArray())
                    .concatWith(Flux.error(Exception("Unexpected exception"))))
        StepVerifier
            .create(
        //WHEN
                repo.findByModel(model))
        //THEN
            .expectNext(*carsWithSameModel.toTypedArray())
            .expectError(Exception::class.java)
            .verify()
        Mockito
            .verify(template)
            .find(
                Query.query(Criteria.where("model").`is`(model)),
                Car::class.java,
                MongoDbReactiveCarRepository.COLLECTION_NAME)
    }

    @Test
    fun shouldDelegateFindByIdCallToMongo() {
        //GIVEN
        val car = fixture.generateCar()
        val id = car.id!!
        Mockito
            .`when`(template.findById(
                id,
                Car::class.java,
                MongoDbReactiveCarRepository.COLLECTION_NAME))
            .thenReturn(Mono.just(car))

        StepVerifier
            .create(
        //WHEN
                repo.findById(id))
        //THEN
            .expectNext(car)
            .verifyComplete()
        Mockito
            .verify(template)
            .findById(
                id,
                Car::class.java,
                MongoDbReactiveCarRepository.COLLECTION_NAME)
    }

    @Test
    fun shouldDelegateSaveCallToMongoTemplate() {
        //GIVEN
        val car = fixture.generateCar()
        Mockito
            .`when`(template.save(
                car,
                MongoDbReactiveCarRepository.COLLECTION_NAME))
            .thenReturn(Mono.just(car))
        StepVerifier
            .create(
        //WHEN
                repo.save(car))
        //THEN
            .expectNext(car)
            .verifyComplete()
        Mockito
            .verify(template)
            .save(
                car,
                MongoDbReactiveCarRepository.COLLECTION_NAME)
    }

    @Test
    fun shouldDelegateFindAllCallToMongoTemplate() {
        //GIVEN
        val cars = fixture.generateCars()
        Mockito
            .`when`(template.findAll(
                Car::class.java,
                MongoDbReactiveCarRepository.COLLECTION_NAME))
            .thenReturn(
                Flux
                    .just(*cars.toTypedArray())
                    .concatWith(Flux.error(Exception("Unexpected exception")))
        )
        StepVerifier
            .create(
        //WHEN
                repo.findAll())
        //THEN
            .expectNext(*cars.toTypedArray())
            .expectError(Exception::class.java)
            .verify()
        Mockito
            .verify(template)
            .findAll(
                Car::class.java,
                MongoDbReactiveCarRepository.COLLECTION_NAME)
    }

    @Test
    fun shouldDelegateDeleteAllCallToMongoTemplate() {
        //GIVEN
        Mockito
            .`when`(template.dropCollection(
                MongoDbReactiveCarRepository.COLLECTION_NAME))
            .thenReturn(Mono.empty())
        StepVerifier
            .create(
        //WHEN
                repo.deleteAll())
        //THEN
            .expectNext(Unit)
            .verifyComplete()
        Mockito
            .verify(template)
            .dropCollection(MongoDbReactiveCarRepository.COLLECTION_NAME)
    }
}