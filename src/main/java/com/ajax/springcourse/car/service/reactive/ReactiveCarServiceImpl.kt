package com.ajax.springcourse.car.service.reactive

import com.ajax.springcourse.car.exception.CarNotFoundException
import com.ajax.springcourse.car.model.dto.CarCreateDto
import com.ajax.springcourse.car.model.dto.CarReadDto
import com.ajax.springcourse.car.model.dto.CarUpdateDto
import com.ajax.springcourse.car.repository.reactive.ReactiveCarRepository
import com.ajax.springcourse.car.service.reactive.ReactiveCarService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Profile
import org.springframework.stereotype.Service
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

@Service
@Profile(value = ["mongo_reactive", "redis_reactive"])
class ReactiveCarServiceImpl @Autowired constructor(val repository: ReactiveCarRepository) : ReactiveCarService {

    override fun findAll(): Flux<CarReadDto> = repository
        .findAll()
        .map(::CarReadDto)

    override fun findByModel(model: String): Flux<CarReadDto> = repository
        .findByModel(model)
        .map(::CarReadDto)

    override fun findById(id: String): Mono<CarReadDto> = repository
        .findById(id)
        .switchIfEmpty(Mono.error { CarNotFoundException("Couldn't find car with id=$id") })
        .map(::CarReadDto)

    override fun create(carCreateDto: CarCreateDto): Mono<CarReadDto> = repository
        .save(carCreateDto.mapToCar())
        .map(::CarReadDto)

    override fun update(carDto: CarUpdateDto): Mono<CarReadDto> = repository
        .findById(carDto.id)
        .switchIfEmpty(Mono.error { CarNotFoundException("Couldn't find car with id=${carDto.id}") })
        .map(carDto::projectOnto)
        .flatMap(repository::save)
        .map(::CarReadDto)

    override fun deleteAll(): Mono<Unit> = repository
        .deleteAll()
}