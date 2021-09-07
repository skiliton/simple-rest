package com.ajax.springcourse.car.service.reactive

import com.ajax.springcourse.car.CarFixture
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
    private val fixture = CarFixture()

    @BeforeEach
    fun beforeEach(){
        repo = Mockito.mock(ReactiveCarRepository::class.java)
        service = ReactiveCarServiceImpl(repo)
    }

    @Test
    fun shouldReturnFluxOfReadDtosOfAllSavedCars() {
        //GIVEN
        val cars = fixture.generateCars()
        Mockito
            .`when`(repo.findAll())
            .thenReturn(
                Flux.just(*cars.toTypedArray()))
        StepVerifier.create(
        //WHEN
            service.findAll())
        //THEN
            .expectNext(*cars.map { CarReadDto(it) }.toTypedArray())
            .verifyComplete()
    }

    @Test
    fun shouldReturnEmptyFluxIfThereIsNoSavedCars() {
        //GIVEN
        Mockito
            .`when`(repo.findAll())
            .thenReturn(Flux.empty())
        StepVerifier
            .create(
        //WHEN
                service.findAll())
        //THEN
            .verifyComplete()
    }

    @Test
    fun shouldReturnFluxOfReadDtosForCarsWithSpecifiedModel() {
        //GIVEN
        val carsWithSameModel = fixture.generateCarsWithSameModel()
        val model = carsWithSameModel.first().model
        Mockito
            .`when`(repo.findByModel(model))
            .thenReturn(
                Flux.just(*carsWithSameModel.toTypedArray()))
        StepVerifier
            .create(
        //WHEN
                service.findByModel(model))
        //THEN
            .expectNext(*carsWithSameModel.map{CarReadDto(it)}.toTypedArray())
            .verifyComplete()
    }

    @Test
    fun shouldReturnEmptyFluxIfSpecifiedModelDoesNotExist() {
        //GIVEN
        Mockito
            .`when`(repo.findByModel("md"))
            .thenReturn(Flux.empty())
        StepVerifier
            .create(
        //WHEN
                service.findByModel("md"))
        //THEN
            .verifyComplete()
    }

    @Test
    fun shouldReturnMonoOfReadDtoForCarsWithSpecifiedId() {
        //GIVEN
        val car = fixture.generateCar()
        Mockito
            .`when`(repo.findById("id1"))
            .thenReturn(Mono.just(
                car))
        StepVerifier
            .create(
        //WHEN
                service.findById("id1"))
        //THEN
            .expectNext(CarReadDto(car))
            .verifyComplete()
    }

    @Test
    fun shouldThrowCarNotFoundExceptionIfThereIsNoCarWithSpecifiedId() {
        //GIVEN
        Mockito
            .`when`(repo.findById("id1"))
            .thenReturn(Mono.empty())
        StepVerifier
            .create(
        //WHEN
                service.findById("id1"))
        //THEN
            .verifyError(CarNotFoundException::class.java)
    }

    @Test
    fun shouldCreateAndReturnReadDtoForCreatedCar() {
        //GIVEN
        val carWithoutId = fixture.generateCarWithoutId()
        val carWithId = fixture.copyWithNewId(carWithoutId)
        val carCreateDto = CarCreateDto(
            carWithoutId.brand,
            carWithoutId.model,
            carWithoutId.seats,
            carWithoutId.description)
        val carReadDto = CarReadDto(carWithId)
        Mockito
            .`when`(repo.save(carWithoutId))
            .thenReturn(Mono.just(carWithId))
        StepVerifier
            .create(
        //WHEN
                service.create(carCreateDto))
        //THEN
            .expectNext(carReadDto)
            .verifyComplete()
    }

    @Test
    fun shouldUpdateAndReturnReadDtoUpdatedCar() {
        //GIVEN
        val car = fixture.generateCar()
        val carWithNewDescription = fixture.copyWithNewDescription(car)
        val carUpdateDto = CarUpdateDto(id = car.id!!, description = carWithNewDescription.description)
        Mockito
            .`when`(repo.findById(car.id!!))
            .thenReturn(Mono.just(
                car))
        Mockito
            .`when`(repo.save(
                carWithNewDescription))
            .thenReturn(Mono.just(
                carWithNewDescription))
        StepVerifier
            .create(
        //WHEN
                service.update(carUpdateDto))
        //THEN
            .expectNext(CarReadDto(carWithNewDescription))
            .verifyComplete()
    }

    @Test
    fun shouldThrowCarNotFoundExceptionIfThereIsNoSuchCarToUpdate() {
        //GIVEN
        Mockito
            .`when`(repo.findById("idDoesNotExists"))
            .thenReturn(Mono.empty())
        StepVerifier
            .create(
        //WHEN
                service.update(CarUpdateDto(id = "idDoesNotExists", description = "ds2")))
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
        StepVerifier
            .create(
        //WHEN
            service.deleteAll())
        //THEN
            .verifyComplete()
        Mockito
            .verify(repo)
            .deleteAll()
    }

}