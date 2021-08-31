package com.ajax.springcourse.car.controller

import com.ajax.springcourse.car.model.dto.CarCreateDto
import com.ajax.springcourse.car.model.dto.CarReadDto
import com.ajax.springcourse.car.model.dto.CarUpdateDto
import com.ajax.springcourse.car.service.ReactiveCarService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Profile
import org.springframework.stereotype.Controller
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import javax.validation.Valid

@Controller
@Profile(value = ["mongo_reactive", "redis_reactive"])
class ReactiveCarController @Autowired constructor(val service: ReactiveCarService){
    fun findAll(): Flux<CarReadDto> = service.findAll()

    fun findByModel(model: String): Flux<CarReadDto> = service.findByModel(model)

    fun create(carCreateDto: @Valid CarCreateDto): Mono<CarReadDto> = service.create(carCreateDto)

    fun findById(id: String): Mono<CarReadDto> = service.findById(id)

    fun update(carUpdateDto: @Valid CarUpdateDto): Mono<CarReadDto> = service.update(carUpdateDto)
}