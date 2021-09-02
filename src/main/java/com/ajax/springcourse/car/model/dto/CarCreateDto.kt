package com.ajax.springcourse.car.model.dto

import com.ajax.springcourse.car.model.Car
import javax.validation.constraints.Min

class CarCreateDto(
    var brand: String = "",
    var model: String = "",
    var seats: @Min(1) Int = 1,
    var description: String = ""
) {
    fun mapToCar(): Car = Car(
        brand = brand,
        model = model,
        seats = seats,
        description = description
    )
}