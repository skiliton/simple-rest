package com.ajax.springcourse.car.model;

import lombok.Data;

import java.util.Optional;

@Data
public class CarUpdateDto {
    private String brand;
    private String model;
    private Integer seats;
    private String description;

    public void projectOnto(Car car){
        Optional.ofNullable(brand).ifPresent(car::setBrand);
        Optional.ofNullable(model).ifPresent(car::setModel);
        Optional.ofNullable(seats).ifPresent(car::setSeats);
        Optional.ofNullable(description).ifPresent(car::setDescription);
    }
}
