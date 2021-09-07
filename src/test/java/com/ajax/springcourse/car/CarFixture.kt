package com.ajax.springcourse.car

import com.ajax.springcourse.car.model.Car
import org.jeasy.random.EasyRandom
import kotlin.random.Random

class CarFixture {

    private val generator = EasyRandom()

    fun generateCar(): Car = generator.nextObject(Car::class.java)

    fun generateCarWithModel(model: String): Car {
        val car = generator.nextObject(Car::class.java)
        car.model = model
        return car
    }

    fun generateCarsWithSameModel(): List<Car> {
        val model = generator.nextObject(String::class.java)
        return generateSequence { generateCarWithModel(model) }
            .take(Random.nextInt(1,1000))
            .toList()
    }

    fun generateCars(): List<Car> =
        generateSequence(this::generateCar)
            .take(Random.nextInt(1,1000))
            .toList()


    fun generateCarWithoutId(): Car {
        val car = generator.nextObject(Car::class.java)
        car.id = null
        return car
    }

    fun copy(car: Car) =
        Car(
            car.id,
            car.brand,
            car.model,
            car.seats,
            car.description
        )

    fun copyWithNewId(car: Car): Car {
        val copy = copy(car)
        copy.id = generator.nextObject(String::class.java)
        return copy
    }


    fun copyWithNewDescription(car: Car): Car {
        val copy = copy(car)
        copy.description = generator.nextObject(String::class.java)
        return copy
    }


    fun copyWithNewModel(car: Car): Car {
        val copy = copy(car)
        copy.model = generator.nextObject(String::class.java)
        return copy
    }
}