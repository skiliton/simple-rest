package com.ajax.springcourse.car.model.dto;

import com.ajax.springcourse.car.model.Car;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

@Getter
@Setter
@NoArgsConstructor
public class CarCreateDto {

    @NotNull
    private String brand;
    @NotNull
    private String model;
    @NotNull
    @Min(1)
    private int seats;
    @NotNull
    private String description;

    public CarCreateDto(Car car) {
        brand = car.getBrand();
        model = car.getModel();
        seats = car.getSeats();
        description = car.getDescription();
    }

    public Car mapToCar() {
        return new Car(brand, model, seats, description);
    }
}
