package com.ajax.springcourse.car.repository;

import com.ajax.springcourse.car.model.Car;
import org.springframework.data.repository.CrudRepository;

public interface CarRepository extends CrudRepository<Car,Long> {
    public Car findByModel(String model);
}
