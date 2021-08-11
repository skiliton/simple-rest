package com.ajax.springcourse.car.service;

import com.ajax.springcourse.car.model.Car;
import com.ajax.springcourse.car.model.dto.CarDto;
import com.ajax.springcourse.car.model.dto.CarUpdateDto;

import java.util.List;

public interface CarService {
    List<CarDto> findAll();
    CarDto findByModel(String model);
    CarDto findById(String id);
    Car create(CarDto carDto);
    CarDto update(CarUpdateDto carDto);
}