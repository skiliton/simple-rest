package com.ajax.springcourse.car.service

import com.ajax.springcourse.car.model.dto.CarCreateDto
import com.ajax.springcourse.car.model.dto.CarReadDto
import com.ajax.springcourse.car.model.dto.CarUpdateDto
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

interface ReactiveCarService {
    fun findAll(): Flux<CarReadDto>

    fun findByModel(model: String): Flux<CarReadDto>

    fun findById(id: String): Mono<CarReadDto>

    fun create(carCreateDto: CarCreateDto): Mono<CarReadDto>

    fun update(carDto: CarUpdateDto): Mono<CarReadDto>

    fun deleteAll(): Mono<Unit>
}