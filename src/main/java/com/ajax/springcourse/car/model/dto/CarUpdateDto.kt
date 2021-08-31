package com.ajax.springcourse.car.model.dto

import com.ajax.springcourse.car.model.Car

data class CarUpdateDto(var id: String = "", var description: String = "") {

    fun projectOnto(car: Car): Car {
        car.description = description
        return car
    }
}