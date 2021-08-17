package com.ajax.springcourse.car.service;

import com.ajax.springcourse.car.model.dto.CarCreateDto;
import com.ajax.springcourse.car.model.dto.CarReadDto;
import com.ajax.springcourse.car.model.dto.CarUpdateDto;

import java.util.List;

public interface CarService {
    List<CarReadDto> findAll();

    List<CarReadDto> findByModel(String model);

    CarReadDto findById(String id);

    CarReadDto create(CarCreateDto carCreateDto);

    CarReadDto update(CarUpdateDto carDto);
}