package com.ajax.springcourse.car.service;

import com.ajax.springcourse.car.model.Car;
import com.ajax.springcourse.car.model.CarCreateDto;
import com.ajax.springcourse.car.model.CarUpdateDto;
import com.ajax.springcourse.car.repository.CarRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CarServiceImpl implements CarService{

    private final CarRepository repository;

    @Autowired
    public CarServiceImpl(CarRepository repository) {
        this.repository = repository;
    }

    @Override
    public Iterable<Car> findAll() {
        return repository.findAll();
    }

    @Override
    public Car findByModel(String model) {
        return repository.findByModel(model);
    }

    @Override
    public Car create(CarCreateDto carDto) {
        Car car = carDto.mapToCar();
        return repository.save(car);
    }

    @Override
    public void update(CarUpdateDto carDto, long id){
        Car car = repository.findById(id).orElseThrow();
        carDto.projectOnto(car);
        repository.save(car);
    }
}
