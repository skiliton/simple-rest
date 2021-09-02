package com.ajax.springcourse.car.repository.reactive

import com.ajax.springcourse.car.model.Car
import org.junit.jupiter.api.Test
import org.mockito.Mockito
import org.springframework.data.mongodb.core.ReactiveMongoTemplate
import org.springframework.data.mongodb.core.query.Criteria
import org.springframework.data.mongodb.core.query.Query
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import reactor.test.StepVerifier

class MongoDbReactiveCarRepositoryTest {

    @Test
    fun Given_MongoTemplate_When_AskedToFindByModel_Then_DelegateCallToMongo() {
        val template = Mockito.mock(ReactiveMongoTemplate::class.java)
        Mockito.`when`(
            template.find(
                Query.query(Criteria.where("model").`is`("md")),
                Car::class.java,
                MongoDbReactiveCarRepository.COLLECTION_NAME
            )
        ).thenReturn(
            Flux.just(
                Car("id1", "br1", "md", 1, "ds1"),
                Car("id2", "br2", "md", 2, "ds2"),
                Car("id3", "br3", "md", 3, "ds3")
            ).concatWith(Flux.error(Exception("Unexpected exception")))
        )

        val repo = MongoDbReactiveCarRepository(template)

        StepVerifier.create(repo.findByModel("md"))
            .expectNext(Car("id1", "br1", "md", 1, "ds1"))
            .expectNext(Car("id2", "br2", "md", 2, "ds2"))
            .expectNext(Car("id3", "br3", "md", 3, "ds3"))
            .expectError(Exception::class.java)
            .verify()
        Mockito.verify(template).find(
            Query.query(Criteria.where("model").`is`("md")),
            Car::class.java,
            MongoDbReactiveCarRepository.COLLECTION_NAME
        )
    }

    @Test
    fun Given_MongoTemplate_When_AskedToFindById_Then_DelegateCallToMongo() {
        val template = Mockito.mock(ReactiveMongoTemplate::class.java)
        Mockito.`when`(
            template.findById(
                "id",
                Car::class.java,
                MongoDbReactiveCarRepository.COLLECTION_NAME
            )
        ).thenReturn(
            Mono.just(
                Car("id", "br", "md", 1, "ds")
            )
        )

        val repo = MongoDbReactiveCarRepository(template)

        StepVerifier.create(repo.findById("id"))
            .expectNext(Car("id", "br", "md", 1, "ds"))
            .verifyComplete()
        Mockito.verify(template).findById(
            "id",
            Car::class.java,
            MongoDbReactiveCarRepository.COLLECTION_NAME
        )
    }

    @Test
    fun Given_MongoTemplate_When_AskedToSave_Then_DelegateCallToMongoTemplate() {
        val template = Mockito.mock(ReactiveMongoTemplate::class.java)
        Mockito.`when`(
            template.save(
                Car("id", "br", "md", 1, "ds"),
                MongoDbReactiveCarRepository.COLLECTION_NAME
            )
        ).thenReturn(
            Mono.just(
                Car("id", "br", "md", 1, "ds")
            )
        )

        val repo = MongoDbReactiveCarRepository(template)

        StepVerifier.create(
            repo.save(
                Car("id", "br", "md", 1, "ds")
            )
        ).expectNext(
            Car("id", "br", "md", 1, "ds")
        ).verifyComplete()

        Mockito.verify(template).save(
            Car("id", "br", "md", 1, "ds"),
            MongoDbReactiveCarRepository.COLLECTION_NAME
        )
    }

    @Test
    fun Given_MongoTemplate_When_AskedToFindAll_Then_DelegateCallToMongoTemplate() {
        val template = Mockito.mock(ReactiveMongoTemplate::class.java)
        Mockito.`when`(
            template.findAll(
                Car::class.java,
                MongoDbReactiveCarRepository.COLLECTION_NAME
            )
        ).thenReturn(
            Flux.just(
                Car("id1", "br1", "md1", 1, "ds1"),
                Car("id2", "br2", "md2", 2, "ds2"),
                Car("id3", "br3", "md3", 3, "ds3"),
            ).concatWith(Flux.error(Exception("Unexpected exception")))
        )

        val repo = MongoDbReactiveCarRepository(template)
        StepVerifier.create(repo.findAll())
            .expectNext(Car("id1", "br1", "md1", 1, "ds1"))
            .expectNext(Car("id2", "br2", "md2", 2, "ds2"))
            .expectNext(Car("id3", "br3", "md3", 3, "ds3"))
            .expectError(Exception::class.java)
            .verify()
        Mockito.verify(template).findAll(
            Car::class.java,
            MongoDbReactiveCarRepository.COLLECTION_NAME
        )
    }

    @Test
    fun Given_MongoTemplate_When_AskedToDeleteAll_Then_DelegateCallToMongoTemplate() {
        val template = Mockito.mock(ReactiveMongoTemplate::class.java)
        Mockito.`when`(
            template.dropCollection(
                MongoDbReactiveCarRepository.COLLECTION_NAME
            )
        ).thenReturn(Mono.empty())

        val repo = MongoDbReactiveCarRepository(template)

        StepVerifier.create(repo.deleteAll()).verifyComplete()
        Mockito.verify(template).dropCollection(
            MongoDbReactiveCarRepository.COLLECTION_NAME
        )
    }
}