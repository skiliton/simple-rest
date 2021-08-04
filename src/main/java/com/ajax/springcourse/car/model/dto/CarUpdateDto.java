package com.ajax.springcourse.car.model.dto;

import com.ajax.springcourse.car.model.Car;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class CarUpdateDto {

    @NotNull
    private String description;

    public void projectOnto(Car car){
        car.setDescription(description);
    }
}
