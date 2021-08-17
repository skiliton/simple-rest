package com.ajax.springcourse.car.migration;

import com.ajax.springcourse.car.model.Car;
import com.ajax.springcourse.car.repository.CarRepository;

import java.util.Collections;
import java.util.List;

public class CarMigrationImpl implements CarMigration {
    private final CarRepository fromRepository;
    private final CarRepository toRepository;

    public CarMigrationImpl(CarRepository from, CarRepository to) {
        this.fromRepository = from;
        this.toRepository = to;
    }

    @Override
    public List<Car> migrate() {
        toRepository.deleteAll();
        fromRepository
                .findAll()
                .stream()
                .peek(car -> car.setId(null))
                .forEach(toRepository::save);
        List<Car> carsFrom = fromRepository.findAll();
        List<Car> carsTo = toRepository.findAll();
        if (carsFrom.containsAll(carsTo) && carsTo.containsAll(carsFrom)) {
            return Collections.emptyList();
        }
        carsTo.removeAll(carsFrom);
        return carsTo;
    }
}
