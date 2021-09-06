package com.ajax.springcourse.car.repository.reactive

import com.ajax.springcourse.car.model.Car
import org.junit.Ignore
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.Mockito
import org.springframework.data.mongodb.core.ReactiveMongoTemplate
import org.springframework.data.redis.connection.ReactiveRedisConnection
import org.springframework.data.redis.connection.ReactiveServerCommands
import org.springframework.data.redis.core.ReactiveHashOperations
import org.springframework.data.redis.core.ReactiveRedisTemplate
import org.springframework.data.redis.core.ReactiveSetOperations
import org.springframework.data.redis.core.ReactiveValueOperations
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import reactor.test.StepVerifier

class RedisReactiveCarRepositoryTest{

    lateinit var template:  ReactiveRedisTemplate<String,String>
    lateinit var hashOps: ReactiveHashOperations<String, String, String>
    lateinit var setOps: ReactiveSetOperations<String, String>
    lateinit var repo: RedisReactiveCarRepository

    @BeforeEach
    fun beforeEach(){
        template = Mockito.mock(ReactiveRedisTemplate::class.java) as ReactiveRedisTemplate<String,String>
        hashOps = Mockito.mock(ReactiveHashOperations::class.java) as ReactiveHashOperations<String, String, String>
        setOps = Mockito.mock(ReactiveSetOperations::class.java) as ReactiveSetOperations<String, String>

        Mockito
            .`when`(template.opsForHash<String,String>())
            .thenReturn(hashOps)

        Mockito
            .`when`(template.opsForSet())
            .thenReturn(setOps)

        repo = RedisReactiveCarRepository(template)
        repo.init()
    }

    @Test
    fun shouldReturnFluxOfCarsWithRequetsedModel(){
        //GIVEN
        Mockito
            .`when`(
                setOps.members("indexes:cars:model:md"))
            .thenReturn(Flux.just("id1","id2"))
        Mockito
            .`when`(
                hashOps.entries("cars:id1"))
            .thenReturn(
                Flux.just(*mapOf(
                    "id" to "id1",
                    "brand" to "br1",
                    "model" to "md",
                    "seats" to "1",
                    "description" to "ds1"
                ).entries.toTypedArray()))
        Mockito
            .`when`(
                hashOps.entries("cars:id2"))
            .thenReturn(
                Flux.just(*mapOf(
                    "id" to "id2",
                    "brand" to "br2",
                    "model" to "md",
                    "seats" to "2",
                    "description" to "ds2"
                ).entries.toTypedArray()))

        StepVerifier.create(
        //WHEN
            repo.findByModel("md")
        )
        //THEN
        .expectNext(Car("id1","br1","md",1,"ds1"))
        .expectNext(Car("id2","br2","md",2,"ds2"))
        .verifyComplete()
    }

    @Test
    fun shouldReturnEmptyFluxIfNoCarsWithSpecifiedModelExist(){
        //GIVEN
        Mockito
            .`when`(
                setOps.members("indexes:cars:model:md"))
            .thenReturn(Flux.empty())
        StepVerifier.create(
        //WHEN
            repo.findByModel("md")
        )
        //THEN
        .verifyComplete()
        Mockito.verify(hashOps,Mockito.never()).entries(Mockito.any())
    }

    @Test
    fun shouldReturnMonoOfCarWithSpecifiedId(){
        //GIVEN
        Mockito
            .`when`(
                hashOps.entries("cars:id"))
            .thenReturn(
                Flux.just(*mapOf(
                    "id" to "id",
                    "brand" to "br",
                    "model" to "md",
                    "seats" to "1",
                    "description" to "ds"
                ).entries.toTypedArray()))

        StepVerifier.create(
        //WHEN
            repo.findById("id")
        )
        //THEN
        .expectNext(Car("id","br","md",1,"ds"))
        .verifyComplete()
    }

    @Test
    fun shouldReturnEmptyMonoIfCarWithSpecifiedIdDoesNotExist(){
        //GIVEN
        Mockito
            .`when`(hashOps.entries(Mockito.any()))
            .thenReturn(Flux.empty())
        StepVerifier.create(
        //WHEN
            repo.findById("id")
        )
        //THEN
        .verifyComplete()
    }

    @Test
    fun shouldSetIdPropertyAndCreateModelIndexAndSaveToHash(){
        //GIVEN
        Mockito
            .`when`(setOps.add(Mockito.eq("indexes:cars:model:md"),Mockito.anyString()))
            .thenReturn(Mono.just(1L))
        Mockito
            .`when`(hashOps
                .putAll(Mockito.contains("cars:"), Mockito.argThat{map ->
                    map["brand"] == "br"&&
                    map["model"] == "md"&&
                    map["seats"] == "1"&&
                    map["description"]=="ds"
                }))
            .thenReturn(Mono.just(false))
        StepVerifier.create(
        //WHEN
            repo.save(Car(
                brand = "br",
                model = "md",
                seats = 1,
                description = "ds"
            ))
        )
        //THEN
        .expectNextMatches{ car ->
            car.brand == "br"&&
            car.model == "md"&&
            car.seats == 1&&
            car.description == "ds"
        }.verifyComplete()
        Mockito.verify(setOps)
            .add(Mockito.eq("indexes:cars:model:md"),Mockito.anyString())
        Mockito.verify(hashOps)
            .putAll(Mockito.contains("cars:"), Mockito.argThat{map ->
                map["brand"] == "br"&&
                        map["model"] == "md"&&
                        map["seats"] == "1"&&
                        map["description"]=="ds"
            })
    }

    @Test
    fun shouldDeleteOldIndexAndCreateNewModelIndexAndSaveToHash(){
        //GIVEN
        Mockito
            .`when`(setOps.remove("indexes:cars:model:md1","cars:id"))
            .thenReturn(Mono.just(1L))
        Mockito
            .`when`(setOps.add("indexes:cars:model:md2","cars:id"))
            .thenReturn(Mono.just(1L))
        Mockito
            .`when`(hashOps.entries("cars:id"))
            .thenReturn(Flux.just(*mapOf(
                "id" to "id",
                "brand" to "br",
                "model" to "md1",
                "seats" to "1",
                "description" to "ds"
            ).entries.toTypedArray()))
        Mockito
            .`when`(hashOps
                .putAll("cars:id", mutableMapOf(
                    "id" to "id",
                    "brand" to "br",
                    "model" to "md2",
                    "seats" to "1",
                    "description" to "ds"
                )))
            .thenReturn(Mono.just(false))
        StepVerifier.create(
        //WHEN
            repo.save(Car("id","br","md2",1,"ds"))
        )
        //THEN
        .expectNext(Car("id","br","md2",1,"ds"))
        .verifyComplete()
    }



    @Test
    fun shouldReturnFluxOfAllCars(){
        //GIVEN
        Mockito
            .`when`(
                template.keys("cars:*"))
            .thenReturn(Flux.just("cars:id1","cars:id2"))
        Mockito
            .`when`(
                hashOps.entries("cars:id1"))
            .thenReturn(
                Flux.just(*mapOf(
                    "id" to "id1",
                    "brand" to "br1",
                    "model" to "md1",
                    "seats" to "1",
                    "description" to "ds1"
                ).entries.toTypedArray()))
        Mockito
            .`when`(
                hashOps.entries("cars:id2"))
            .thenReturn(
                Flux.just(*mapOf(
                    "id" to "id2",
                    "brand" to "br2",
                    "model" to "md2",
                    "seats" to "2",
                    "description" to "ds2"
                ).entries.toTypedArray()))
        StepVerifier.create(
        //WHEN
            repo.findAll()
        )
        //THEN
        .expectNext(Car("id1","br1","md1",1,"ds1"))
        .expectNext(Car("id2","br2","md2",2,"ds2"))
        .verifyComplete()
    }

    @Test
    fun shouldReturnEmptyFluxIfThereIsNoSavedCars(){
        //GIVEN
        Mockito
            .`when`(
                template.keys("cars:*"))
            .thenReturn(Flux.empty())
        StepVerifier.create(
        //WHEN
            repo.findAll()
        )
        //THEN
        .verifyComplete()
        Mockito.verify(hashOps,Mockito.never()).entries(Mockito.any())
    }

    @Test
    fun shouldDeleteAllHashesAndAllIndexes() {
        //GIVEN
        Mockito
            .`when`(template.keys("cars:*"))
            .thenReturn(Flux.just("cars:id1","cars:id2","cars:id3"))
        Mockito
            .`when`(template.keys("indexes:cars:model:*"))
            .thenReturn(Flux.just("indexes:cars:model:md1","indexes:cars:model:md2"))
        Mockito
            .`when`(template.delete(Mockito.any<Flux<String>>()))
            .thenReturn(Mono.empty())
        StepVerifier.create(
        //WHEN
            repo.deleteAll()
        )
        //THEN
        .verifyComplete()
        Mockito.verify(template,Mockito.times(2)).delete(Mockito.any<Flux<String>>())
    }
}