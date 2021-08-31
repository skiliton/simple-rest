package com.ajax.springcourse.car.model

import org.springframework.data.annotation.Version
import java.io.Serializable

class Car(
    var brand: String = "",
    var model: String = "",
    var seats: Int = 1,
    var description: String = ""
) : Serializable {
    var id: String? = null
    @Version
    var version: Long = 0
}