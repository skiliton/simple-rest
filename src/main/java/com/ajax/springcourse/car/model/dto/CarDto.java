package com.ajax.springcourse.car.model.dto;

import com.ajax.springcourse.car.model.Car;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

@Data
@NoArgsConstructor
public class CarDto {

    @NotNull
    private String brand;
    @NotNull
    private String model;
    @NotNull
    @Min(1)
    private int seats;
    @NotNull
    private String description;

    public CarDto(Car car){
        brand = car.getBrand();
        model = car.getModel();
        seats = car.getSeats();
        description = car.getDescription();
    }

    public Car mapToCar(){
        return new Car(brand,model,seats,description);
    }
}
