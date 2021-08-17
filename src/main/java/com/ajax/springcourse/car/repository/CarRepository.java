package com.ajax.springcourse.car.repository;

import com.ajax.springcourse.car.model.Car;

import java.util.List;
import java.util.Optional;

public interface CarRepository {
    List<Car> findByModel(String model);

    Optional<Car> findById(String id);

    Car save(Car car);

    List<Car> findAll();
}
