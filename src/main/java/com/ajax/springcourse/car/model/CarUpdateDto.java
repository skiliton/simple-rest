package com.ajax.springcourse.car.model;

import java.util.Optional;

public class CarUpdateDto {
    private Optional<String> brand;
    private Optional<String> model;
    private Optional<Integer> seats;
    private Optional<String> description;

    public void projectOnto(Car car){
        brand.ifPresent(car::setBrand);
        model.ifPresent(car::setModel);
        seats.ifPresent(car::setSeats);
        description.ifPresent(car::setDescription);
    }
}
