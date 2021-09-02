package com.ajax.springcourse.car.model.dto

import com.ajax.springcourse.car.model.Car

data class CarReadDto(
    val id: String? = null,
    val brand: String,
    val model: String,
    val seats: Int,
    val description: String
){
    constructor(car: Car): this(car.id, car.brand, car.model, car.seats, car.description)
}