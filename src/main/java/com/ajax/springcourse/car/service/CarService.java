package com.ajax.springcourse.car.service;

import com.ajax.springcourse.car.model.Car;
import com.ajax.springcourse.car.model.dto.CarDto;
import com.ajax.springcourse.car.model.dto.CarUpdateDto;

import java.util.List;

public interface CarService {
    public List<CarDto> findAll();
    public CarDto findByModel(String model);
    public CarDto findById(long id);
    public CarDto create(CarDto carDto);
    public CarDto update(CarUpdateDto carDto, long id);
}