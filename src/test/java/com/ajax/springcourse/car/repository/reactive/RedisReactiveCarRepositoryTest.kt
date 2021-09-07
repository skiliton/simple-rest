package com.ajax.springcourse.car.repository.reactive

import com.ajax.springcourse.car.CarFixture
import com.ajax.springcourse.car.model.Car
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.Mockito
import org.springframework.data.redis.core.ReactiveHashOperations
import org.springframework.data.redis.core.ReactiveRedisTemplate
import org.springframework.data.redis.core.ReactiveSetOperations
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import reactor.test.StepVerifier

class RedisReactiveCarRepositoryTest {

    lateinit var template: ReactiveRedisTemplate<String, String>
    lateinit var hashOps: ReactiveHashOperations<String, String, String>
    lateinit var setOps: ReactiveSetOperations<String, String>
    lateinit var repo: RedisReactiveCarRepository
    private val fixture = CarFixture()

    @BeforeEach
    fun beforeEach() {
        template = Mockito.mock(ReactiveRedisTemplate::class.java) as ReactiveRedisTemplate<String, String>
        hashOps = Mockito.mock(ReactiveHashOperations::class.java) as ReactiveHashOperations<String, String, String>
        setOps = Mockito.mock(ReactiveSetOperations::class.java) as ReactiveSetOperations<String, String>
        Mockito
            .`when`(template.opsForHash<String, String>())
            .thenReturn(hashOps)
        Mockito
            .`when`(template.opsForSet())
            .thenReturn(setOps)
        repo = RedisReactiveCarRepository(template)
        repo.init()
    }

    @Test
    fun shouldReturnFluxOfCarsWithRequetsedModel() {
        //GIVEN
        val carWithSameModel = fixture.generateCarsWithSameModel()
        val model = carWithSameModel.first().model
        Mockito
            .`when`(setOps.members("indexes:cars:model:$model"))
            .thenReturn(
                Flux.just(*carWithSameModel.map { it.id }.toTypedArray()))
        Mockito
            .`when`(hashOps.entries(Mockito.contains("cars:")))
            .thenAnswer{ inv ->
                val str: String = inv.arguments[0] as String
                val id = """^cars:(.*)""".toRegex().find(str)!!.groupValues[1]
                val car = carWithSameModel.first { it.id == id }
                Flux.just(
                    *mapOf(
                        "id" to car.id,
                        "brand" to car.brand,
                        "model" to car.model,
                        "seats" to car.seats.toString(),
                        "description" to car.description).entries.toTypedArray())
            }

        StepVerifier
            .create(
        //WHEN
                repo.findByModel(model))
        //THEN
            .expectNext(*carWithSameModel.toTypedArray())
            .verifyComplete()
    }

    @Test
    fun shouldReturnEmptyFluxIfNoCarsWithSpecifiedModelExist() {
        //GIVEN
        Mockito
            .`when`(setOps.members("indexes:cars:model:md"))
            .thenReturn(Flux.empty())
        StepVerifier
            .create(
        //WHEN
                repo.findByModel("md"))
        //THEN
            .verifyComplete()
        Mockito
            .verify(hashOps, Mockito.never())
            .entries(Mockito.any())
    }

    @Test
    fun shouldReturnMonoOfCarWithSpecifiedId() {
        //GIVEN
        val car = fixture.generateCar()
        val id = car.id
        Mockito
            .`when`(
                hashOps.entries("cars:$id"))
            .thenReturn(
                Flux.just(
                    *mapOf(
                        "id" to id,
                        "brand" to car.brand,
                        "model" to car.model,
                        "seats" to car.seats.toString(),
                        "description" to car.description).entries.toTypedArray()))
        StepVerifier
            .create(
        //WHEN
                repo.findById(id!!))
        //THEN
            .expectNext(car)
            .verifyComplete()
    }

    @Test
    fun shouldReturnEmptyMonoIfCarWithSpecifiedIdDoesNotExist() {
        //GIVEN
        Mockito
            .`when`(hashOps.entries(Mockito.any()))
            .thenReturn(Flux.empty())
        StepVerifier
            .create(
        //WHEN
                repo.findById("id"))
        //THEN
            .verifyComplete()
    }

    @Test
    fun shouldSetIdPropertyAndCreateModelIndexAndSaveToHash() {
        //GIVEN
        val carWithoutId = fixture.generateCarWithoutId()
        val model = carWithoutId.model
        Mockito
            .`when`(setOps.add(Mockito.eq("indexes:cars:model:$model"), Mockito.anyString()))
            .thenReturn(Mono.just(1L))
        Mockito
            .`when`(
                hashOps
                    .putAll(
                        Mockito.contains("cars:"),
                        Mockito.argThat { map ->
                            map["brand"] == carWithoutId.brand &&
                                    map["model"] == carWithoutId.model &&
                                    map["seats"] == carWithoutId.seats.toString() &&
                                    map["description"] == carWithoutId.description
                        }))
            .thenReturn(Mono.just(false))
        StepVerifier
            .create(
        //WHEN
                repo.save(carWithoutId))
        //THEN
            .expectNextMatches { car ->
                car.brand == carWithoutId.brand &&
                        car.model == carWithoutId.model &&
                        car.seats == carWithoutId.seats &&
                        car.description == carWithoutId.description
            }
            .verifyComplete()
        Mockito.verify(setOps)
            .add(Mockito.eq("indexes:cars:model:$model"), Mockito.anyString())
        Mockito
            .verify(hashOps)
            .putAll(
                Mockito.contains("cars:"),
                Mockito.argThat { map ->
                    map["brand"] == carWithoutId.brand &&
                            map["model"] == carWithoutId.model &&
                            map["seats"] == carWithoutId.seats.toString() &&
                            map["description"] == carWithoutId.description
                })
    }

    @Test
    fun shouldDeleteOldIndexAndCreateNewModelIndexAndSaveToHash() {
        //GIVEN
        val carWithOldModel = fixture.generateCar()
        val oldModel = carWithOldModel.model
        val id = carWithOldModel.id
        val carWithNewModel = fixture.copyWithNewModel(carWithOldModel)
        val newModel = carWithNewModel.model
        Mockito
            .`when`(setOps.remove("indexes:cars:model:$oldModel", "cars:$id"))
            .thenReturn(Mono.just(1L))
        Mockito
            .`when`(setOps.add("indexes:cars:model:$newModel", "cars:$id"))
            .thenReturn(Mono.just(1L))
        Mockito
            .`when`(hashOps.entries("cars:$id"))
            .thenReturn(
                Flux.just(
                    *mapOf(
                        "id" to carWithOldModel.id,
                        "brand" to carWithOldModel.brand,
                        "model" to carWithOldModel.model,
                        "seats" to carWithOldModel.seats.toString(),
                        "description" to carWithOldModel.description).entries.toTypedArray()))
        Mockito
            .`when`(hashOps.putAll(
                "cars:$id",
                mutableMapOf(
                    "id" to carWithNewModel.id,
                    "brand" to carWithNewModel.brand,
                    "model" to carWithNewModel.model,
                    "seats" to carWithNewModel.seats.toString(),
                    "description" to carWithNewModel.description
                )))
            .thenReturn(Mono.just(false))
        StepVerifier
            .create(
        //WHEN
                repo.save(carWithNewModel))
        //THEN
            .expectNext(carWithNewModel)
            .verifyComplete()
    }


    @Test
    fun shouldReturnFluxOfAllCars() {
        //GIVEN
        val cars = fixture.generateCars()
        Mockito
            .`when`(template.keys("cars:*"))
            .thenReturn(Flux.just(*cars.map { "cars:${it.id}" }.toTypedArray()))
        Mockito
            .`when`(hashOps.entries(Mockito.contains("cars:")))
            .thenAnswer{ inv ->
                val str: String = inv.arguments[0] as String
                val id = """cars:(.*)""".toRegex().find(str)!!.groupValues[1]
                println(id)
                val car = cars.first { it.id == id }
                Flux.just(
                    *mapOf(
                        "id" to car.id,
                        "brand" to car.brand,
                        "model" to car.model,
                        "seats" to car.seats.toString(),
                        "description" to car.description).entries.toTypedArray())
            }
        StepVerifier
            .create(
        //WHEN
                repo.findAll())
        //THEN
            .expectNext(*cars.toTypedArray())
            .verifyComplete()
    }

    @Test
    fun shouldReturnEmptyFluxIfThereIsNoSavedCars() {
        //GIVEN
        Mockito
            .`when`(
                template.keys("cars:*"))
            .thenReturn(Flux.empty())
        StepVerifier
            .create(
        //WHEN
                repo.findAll())
        //THEN
            .verifyComplete()
        Mockito
            .verify(hashOps, Mockito.never())
            .entries(Mockito.any())
    }

    @Test
    fun shouldDeleteAllHashesAndAllIndexes() {
        //GIVEN
        Mockito
            .`when`(template.keys("cars:*"))
            .thenReturn(Flux.just("cars:id1", "cars:id2", "cars:id3"))
        Mockito
            .`when`(template.keys("indexes:cars:model:*"))
            .thenReturn(Flux.just("indexes:cars:model:md1", "indexes:cars:model:md2"))
        Mockito
            .`when`(template.delete(Mockito.any<Flux<String>>()))
            .thenReturn(Mono.empty())
        StepVerifier
            .create(
        //WHEN
                repo.deleteAll())
        //THEN
            .expectNext(Unit)
            .verifyComplete()
        Mockito
            .verify(template, Mockito.times(2))
            .delete(Mockito.any<Flux<String>>())
    }
}