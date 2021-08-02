package com.ajax.springcourse.car.model;

import lombok.Data;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

@Data
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

    public Car mapToCar(){
        return new Car(brand,model,seats,description);
    }
}
