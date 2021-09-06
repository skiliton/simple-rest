package com.ajax.springcourse.car.repository.reactive

import com.ajax.springcourse.car.model.Car
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

    @BeforeEach
    fun beforeEach(){
        template = Mockito.mock(ReactiveMongoTemplate::class.java)
        repo = MongoDbReactiveCarRepository(template)
    }

    @Test
    fun shouldDelegateFindByModelCallToMongo() {
        //GIVEN
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
        StepVerifier.create(
        //WHEN
            repo.findByModel("md")
        )
        //THEN
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
    fun shouldDelegateFindByIdCallToMongo() {
        //GIVEN
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

        StepVerifier.create(
        //WHEN
            repo.findById("id")
        )
        //THEN
        .expectNext(Car("id", "br", "md", 1, "ds"))
        .verifyComplete()
        Mockito.verify(template).findById(
            "id",
            Car::class.java,
            MongoDbReactiveCarRepository.COLLECTION_NAME
        )
    }

    @Test
    fun shouldDelegateSaveCallToMongoTemplate() {
        //GIVEN
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
        StepVerifier.create(
        //WHEN
            repo.save(Car("id", "br", "md", 1, "ds"))
        )
        //THEN
        .expectNext(Car("id", "br", "md", 1, "ds"))
        .verifyComplete()

        Mockito.verify(template).save(
            Car("id", "br", "md", 1, "ds"),
            MongoDbReactiveCarRepository.COLLECTION_NAME
        )
    }

    @Test
    fun shouldDelegateFindAllCallToMongoTemplate() {
        //GIVEN
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
        StepVerifier.create(
        //WHEN
            repo.findAll()
        )
        //THEN
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
    fun shouldDelegateDeleteAllCallToMongoTemplate() {
        //GIVEN
        Mockito.`when`(
            template.dropCollection(
                MongoDbReactiveCarRepository.COLLECTION_NAME
            )
        ).thenReturn(Mono.empty())
        StepVerifier.create(
        //WHEN
            repo.deleteAll()
        )
        //THEN
        .expectNext(Unit)
        .verifyComplete()
        Mockito.verify(template).dropCollection(
            MongoDbReactiveCarRepository.COLLECTION_NAME
        )
    }
}