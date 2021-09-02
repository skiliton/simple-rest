package com.ajax.springcourse.car.model

import org.springframework.data.annotation.Version
import java.io.Serializable

data class Car(
    var id: String? = null,
    var brand: String = "",
    var model: String = "",
    var seats: Int = 1,
    var description: String = ""
) : Serializable {
    @Version
    var version: Long = 0
}