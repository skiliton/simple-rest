package com.ajax.springcourse.car.repository.reactive

import com.ajax.springcourse.car.model.Car
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

interface ReactiveCarRepository {
    fun findByModel(model: String): Flux<Car>
    fun findById(id: String): Mono<Car>
    fun save(car: Car): Mono<Car>
    fun findAll(): Flux<Car>
    fun deleteAll(): Mono<Unit>
}