package com.ajax.springcourse.car.service.reactive

import com.ajax.springcourse.car.exception.CarNotFoundException
import com.ajax.springcourse.car.model.Car
import com.ajax.springcourse.car.model.dto.CarCreateDto
import com.ajax.springcourse.car.model.dto.CarReadDto
import com.ajax.springcourse.car.model.dto.CarUpdateDto
import com.ajax.springcourse.car.repository.reactive.ReactiveCarRepository
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.Mockito
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import reactor.test.StepVerifier

class ReactiveCarServiceImplTest {

    lateinit var repo: ReactiveCarRepository
    lateinit var service: ReactiveCarServiceImpl

    @BeforeEach
    fun beforeEach(){
        repo = Mockito.mock(ReactiveCarRepository::class.java)
        service = ReactiveCarServiceImpl(repo)
    }

    @Test
    fun shouldReturnFluxOfReadDtosOfAllSavedCars() {
        //GIVEN
        Mockito.`when`(repo.findAll()).thenReturn(
            Flux.just(
                Car("id1", "br1", "md1", 1, "ds1"),
                Car("id2", "br2", "md2", 2, "ds2"),
                Car("id3", "br3", "md3", 3, "ds3"),
                Car("id4", "br4", "md4", 4, "ds4"),
            )
        )
        StepVerifier.create(
        //WHEN
            service.findAll()
        )
        //THEN
            .expectNext(CarReadDto("id1", "br1", "md1", 1, "ds1"))
            .expectNext(CarReadDto("id2", "br2", "md2", 2, "ds2"))
            .expectNext(CarReadDto("id3", "br3", "md3", 3, "ds3"))
            .expectNext(CarReadDto("id4", "br4", "md4", 4, "ds4"))
            .verifyComplete()
    }

    @Test
    fun shouldReturnEmptyFluxIfThereIsNoSavedCars() {
        //GIVEN
        Mockito.`when`(repo.findAll()).thenReturn(Flux.empty())
        StepVerifier.create(
        //WHEN
            service.findAll()
        )
        //THEN
            .verifyComplete()
    }

    @Test
    fun shouldReturnFluxOfReadDtosForCarsWithSpecifiedModel() {
        //GIVEN
        Mockito.`when`(repo.findByModel("md")).thenReturn(
            Flux.just(
                Car("id1", "br1", "md", 1, "ds1"),
                Car("id2", "br2", "md", 2, "ds2"),
                Car("id3", "br3", "md", 3, "ds3"),
                Car("id4", "br4", "md", 4, "ds4"),
            )
        )
        StepVerifier.create(
        //WHEN
            service.findByModel("md")
        )
        //THEN
            .expectNext(CarReadDto("id1", "br1", "md", 1, "ds1"))
            .expectNext(CarReadDto("id2", "br2", "md", 2, "ds2"))
            .expectNext(CarReadDto("id3", "br3", "md", 3, "ds3"))
            .expectNext(CarReadDto("id4", "br4", "md", 4, "ds4"))
            .verifyComplete()
    }

    @Test
    fun shouldReturnEmptyFluxIfSpecifiedModelDoesNotExist() {
        //GIVEN
        Mockito.`when`(repo.findByModel("md")).thenReturn(Flux.empty())
        StepVerifier.create(
        //WHEN
            service.findByModel("md")
        )
        //THEN
            .verifyComplete()
    }

    @Test
    fun shouldReturnMonoOfReadDtoForCarsWithSpecifiedId() {
        //GIVEN
        Mockito.`when`(repo.findById("id1")).thenReturn(
            Mono.just(
                Car("id1", "br1", "md1", 1, "ds1")
            )
        )
        StepVerifier.create(
        //WHEN
            service.findById("id1")
        )
        //THEN
            .expectNext(CarReadDto("id1", "br1", "md1", 1, "ds1"))
            .verifyComplete()
    }

    @Test
    fun shouldThrowCarNotFoundExceptionIfThereIsNoCarWithSpecifiedId() {
        //GIVEN
        Mockito.`when`(repo.findById("id1")).thenReturn(Mono.empty())
        StepVerifier.create(
        //WHEN
            service.findById("id1")
        )
        //THEN
            .verifyError(CarNotFoundException::class.java)
    }

    @Test
    fun shouldCreateAndReturnReadDtoForCreatedCar() {
        //GIVEN
        Mockito
            .`when`(repo.save(Car(brand = "br", model = "md", seats = 1, description = "ds")))
            .thenReturn(Mono.just(Car(id = "id", brand = "br", model = "md", seats = 1, description = "ds")))
        StepVerifier.create(
        //WHEN
            service.create(CarCreateDto(brand = "br", model = "md", seats = 1, description = "ds"))
        )
        //THEN
            .expectNext(CarReadDto("id", "br", "md", 1, "ds"))
            .verifyComplete()
    }

    @Test
    fun shouldUpdateAndReturnReadDtoUpdatedCar() {
        //GIVEN
        Mockito
            .`when`(repo.findById("id"))
            .thenReturn(Mono.just(Car(id = "id", brand = "br1", model = "md1", seats = 1, description = "ds1")))
        Mockito
            .`when`(repo.save(Car(id = "id", brand = "br1", model = "md1", seats = 1, description = "ds2")))
            .thenReturn(Mono.just(Car(id = "id", brand = "br1", model = "md1", seats = 1, description = "ds2")))
        StepVerifier.create(
        //WHEN
            service.update(CarUpdateDto(id = "id", description = "ds2"))
        )
        //THEN
            .expectNext(CarReadDto("id", "br1", "md1", 1, "ds2"))
            .verifyComplete()
    }

    @Test
    fun shouldThrowCarNotFoundExceptionIfThereIsNoSuchCarToUpdate() {
        //GIVEN
        Mockito
            .`when`(repo.findById("idDoesNotExists"))
            .thenReturn(Mono.empty())
        StepVerifier.create(
        //WHEN
            service.update(CarUpdateDto(id = "idDoesNotExists", description = "ds2"))
        )
        //THEN
            .expectError(CarNotFoundException::class.java)
            .verify()
    }

    @Test
    fun shouldDeleteAllFromRepo() {
        //GIVEN
        Mockito
            .`when`(repo.deleteAll())
            .thenReturn(Mono.empty())
        StepVerifier.create(
        //WHEN
            service.deleteAll()
        )
        //THEN
            .verifyComplete()
        Mockito.verify(repo).deleteAll()
    }

}