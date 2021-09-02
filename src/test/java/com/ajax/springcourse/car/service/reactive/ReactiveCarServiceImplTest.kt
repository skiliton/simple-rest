package com.ajax.springcourse.car.service.reactive

import com.ajax.springcourse.car.exception.CarNotFoundException
import com.ajax.springcourse.car.model.Car
import com.ajax.springcourse.car.model.dto.CarCreateDto
import com.ajax.springcourse.car.model.dto.CarReadDto
import com.ajax.springcourse.car.model.dto.CarUpdateDto
import com.ajax.springcourse.car.repository.reactive.ReactiveCarRepository
import org.junit.jupiter.api.Test
import org.mockito.Mockito
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import reactor.test.StepVerifier

class ReactiveCarServiceImplTest {
    @Test
    fun Given_SavedCars_When_AskedToFindAll_Then_ReturnFluxOfReadDtosOfAllSavedCars() {
        val repo = Mockito.mock(ReactiveCarRepository::class.java)
        Mockito.`when`(repo.findAll()).thenReturn(
            Flux.just(
                Car("id1", "br1", "md1", 1, "ds1"),
                Car("id2", "br2", "md2", 2, "ds2"),
                Car("id3", "br3", "md3", 3, "ds3"),
                Car("id4", "br4", "md4", 4, "ds4"),
            )
        )
        val service = ReactiveCarServiceImpl(repo)
        StepVerifier.create(service.findAll())
            .expectNext(CarReadDto("id1", "br1", "md1", 1, "ds1"))
            .expectNext(CarReadDto("id2", "br2", "md2", 2, "ds2"))
            .expectNext(CarReadDto("id3", "br3", "md3", 3, "ds3"))
            .expectNext(CarReadDto("id4", "br4", "md4", 4, "ds4"))
            .verifyComplete()
    }

    @Test
    fun Given_NoSavedCars_When_AskedToFindAll_Then_ReturnEmptyFlux() {
        val repo = Mockito.mock(ReactiveCarRepository::class.java)
        Mockito.`when`(repo.findAll()).thenReturn(Flux.empty())
        val service = ReactiveCarServiceImpl(repo)
        StepVerifier.create(service.findAll())
            .verifyComplete()
    }

    @Test
    fun Given_CarsWithTheSameModel_When_AskedToFindByModel_Then_ReturnFluxOfReadDtosOfCarsWithTheSameModel() {
        val repo = Mockito.mock(ReactiveCarRepository::class.java)
        Mockito.`when`(repo.findByModel("md")).thenReturn(
            Flux.just(
                Car("id1", "br1", "md", 1, "ds1"),
                Car("id2", "br2", "md", 2, "ds2"),
                Car("id3", "br3", "md", 3, "ds3"),
                Car("id4", "br4", "md", 4, "ds4"),
            )
        )
        val service = ReactiveCarServiceImpl(repo)
        StepVerifier.create(service.findByModel("md"))
            .expectNext(CarReadDto("id1", "br1", "md", 1, "ds1"))
            .expectNext(CarReadDto("id2", "br2", "md", 2, "ds2"))
            .expectNext(CarReadDto("id3", "br3", "md", 3, "ds3"))
            .expectNext(CarReadDto("id4", "br4", "md", 4, "ds4"))
            .verifyComplete()
    }

    @Test
    fun Given_NoCarsWithTheSameModel_When_AskedToFindByModel_Then_ReturnEmptyFlux() {
        val repo = Mockito.mock(ReactiveCarRepository::class.java)
        Mockito.`when`(repo.findByModel("md")).thenReturn(Flux.empty())
        val service = ReactiveCarServiceImpl(repo)
        StepVerifier.create(service.findByModel("md"))
            .verifyComplete()
    }

    @Test
    fun Given_CarWithId_When_AskedToFindById_Then_ReturnMonoOfReadDtoForCarsWithId() {
        val repo = Mockito.mock(ReactiveCarRepository::class.java)
        Mockito.`when`(repo.findById("id1")).thenReturn(
            Mono.just(
                Car("id1", "br1", "md1", 1, "ds1")
            )
        )
        val service = ReactiveCarServiceImpl(repo)
        StepVerifier.create(service.findById("id1"))
            .expectNext(CarReadDto("id1", "br1", "md1", 1, "ds1"))
            .verifyComplete()
    }

    @Test
    fun Given_NoCarWithId_When_AskedToFindById_Then_ThrowCarNotFoundException() {
        val repo = Mockito.mock(ReactiveCarRepository::class.java)
        Mockito.`when`(repo.findById("id1")).thenReturn(Mono.empty())
        val service = ReactiveCarServiceImpl(repo)
        StepVerifier.create(service.findById("id1"))
            .verifyError(CarNotFoundException::class.java)
    }

    @Test
    fun Given_CarCreateDto_When_AskedToCreate_Then_CreateAndReturnReadDtoForCreatedCar() {
        val repo = Mockito.mock(ReactiveCarRepository::class.java)
        Mockito
            .`when`(repo.save(Car(brand = "br", model = "md", seats = 1, description = "ds")))
            .thenReturn(Mono.just(Car(id = "id", brand = "br", model = "md", seats = 1, description = "ds")))
        val service = ReactiveCarServiceImpl(repo)
        StepVerifier.create(service.create(CarCreateDto(brand = "br", model = "md", seats = 1, description = "ds")))
            .expectNext(CarReadDto("id", "br", "md", 1, "ds"))
            .verifyComplete()
    }

    @Test
    fun Given_CarUpdateDto_When_AskedToUpdate_Then_UpdateAndReturnReadDtoUpdatedCar() {
        val repo = Mockito.mock(ReactiveCarRepository::class.java)
        Mockito
            .`when`(repo.findById("id"))
            .thenReturn(Mono.just(Car(id = "id", brand = "br1", model = "md1", seats = 1, description = "ds1")))
        Mockito
            .`when`(repo.save(Car(id = "id", brand = "br1", model = "md1", seats = 1, description = "ds2")))
            .thenReturn(Mono.just(Car(id = "id", brand = "br1", model = "md1", seats = 1, description = "ds2")))
        val service = ReactiveCarServiceImpl(repo)
        StepVerifier.create(service.update(CarUpdateDto(id = "id", description = "ds2")))
            .expectNext(CarReadDto("id", "br1", "md1", 1, "ds2"))
            .verifyComplete()
    }

    @Test
    fun Given_CarUpdateDtoWithNonExistenId_When_AskedToUpdate_Then_ThrowCarNotFoundException() {
        val repo = Mockito.mock(ReactiveCarRepository::class.java)
        Mockito
            .`when`(repo.findById("idDoesNotExists"))
            .thenReturn(Mono.empty())
        val service = ReactiveCarServiceImpl(repo)
        StepVerifier.create(service.update(CarUpdateDto(id = "idDoesNotExists", description = "ds2")))
            .expectError(CarNotFoundException::class.java)
            .verify()
    }

    @Test
    fun Given_CarsInRepo_When_AskedToDeleteAll_Then_DeleteAllFromRepo() {
        val repo = Mockito.mock(ReactiveCarRepository::class.java)
        Mockito
            .`when`(repo.deleteAll())
            .thenReturn(Mono.empty())
        val service = ReactiveCarServiceImpl(repo)
        StepVerifier.create(service.deleteAll())
            .verifyComplete()
        Mockito.verify(repo).deleteAll()
    }

}