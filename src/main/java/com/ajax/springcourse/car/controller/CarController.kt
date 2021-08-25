package com.ajax.springcourse.car.controller

import com.ajax.springcourse.car.model.dto.CarCreateDto
import com.ajax.springcourse.car.model.dto.CarReadDto
import com.ajax.springcourse.car.model.dto.CarUpdateDto
import com.ajax.springcourse.car.service.CarService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Profile
import org.springframework.web.bind.annotation.*
import javax.validation.Valid

@RestController
@RequestMapping(path = ["/api/car"], produces = ["application/json"])
@Profile("redis", "mongo")
class CarController @Autowired constructor(private val service: CarService) {
    @GetMapping
    fun findAll(): List<CarReadDto> = service.findAll()

    @GetMapping("/search/find-by-model")
    fun findByModel(@RequestParam model: String?): List<CarReadDto> = service.findByModel(model)

    @PostMapping
    fun create(@RequestBody carCreateDto: @Valid CarCreateDto?): CarReadDto = service.create(carCreateDto)

    @GetMapping("/{id}")
    fun findById(@PathVariable id: String?): CarReadDto = service.findById(id)

    @PatchMapping
    fun update(@RequestBody carUpdateDto: @Valid CarUpdateDto?): CarReadDto = service.update(carUpdateDto)
}