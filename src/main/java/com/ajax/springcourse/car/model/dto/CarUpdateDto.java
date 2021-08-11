package com.ajax.springcourse.car.model.dto;

import com.ajax.springcourse.car.model.Car;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;

@Getter
@Setter
public class CarUpdateDto {

    @NotNull
    private String id;

    @NotNull
    private String description;

    public Car projectOnto(Car car){
        car.setDescription(description);
        return car;
    }
}
