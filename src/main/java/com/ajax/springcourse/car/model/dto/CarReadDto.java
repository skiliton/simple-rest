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
public class CarReadDto {

    private String id;
    private String brand;
    private String model;
    private int seats;
    private String description;

    public CarReadDto(Car car){
        id = car.getId();
        brand = car.getBrand();
        model = car.getModel();
        seats = car.getSeats();
        description = car.getDescription();
    }
}
