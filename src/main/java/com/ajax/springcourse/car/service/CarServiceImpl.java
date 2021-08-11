package com.ajax.springcourse.car.service;

import com.ajax.springcourse.car.model.Car;
import com.ajax.springcourse.car.model.dto.CarDto;
import com.ajax.springcourse.car.model.dto.CarUpdateDto;
import com.ajax.springcourse.car.repository.CarRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CarServiceImpl implements CarService{

    private final CarRepository repository;

    @Autowired
    public CarServiceImpl(CarRepository repository) {
        this.repository = repository;
    }

    @Override
    public List<CarDto> findAll() {
        return repository
            .findAll()
            .stream()
            .map(CarDto::new)
            .collect(Collectors.toList())
        ;
    }

    @Override
    public CarDto findByModel(String model) {
        return new CarDto(repository.findByModel(model).orElseThrow());
    }

    @Override
    public CarDto findById(String id) {
        return new CarDto(repository.findById(id).orElseThrow());
    }

    @Override
    public Car create(CarDto carDto) {
        Car car = carDto.mapToCar();
        return repository.save(car);
    }

    @Override
    public CarDto update(CarUpdateDto carDto){
        Car car = repository.findById(carDto.getId()).orElseThrow();
        return new CarDto(repository.save(carDto.projectOnto(car)));
    }
}
