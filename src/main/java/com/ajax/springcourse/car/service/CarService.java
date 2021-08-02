package com.ajax.springcourse.car.service;

import com.ajax.springcourse.car.model.Car;
import com.ajax.springcourse.car.model.CarCreateDto;
import com.ajax.springcourse.car.model.CarUpdateDto;

public interface CarService {
    public Iterable<Car> findAll();
    public Car findByModel(String model);
    public Car create(CarCreateDto carDto);
    public void update(CarUpdateDto carDto, long id);
}