package com.ajax.springcourse.car.repository;

import com.ajax.springcourse.car.model.Car;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface CarRepository extends JpaRepository<Car,Long> {
    public Car findByModel(String model);
}
