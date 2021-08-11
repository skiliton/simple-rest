package com.ajax.springcourse.car.model;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Version;

@Setter
@Getter
public class Car {
    private String id;
    @Version
    private long version;
    private String brand;
    private String model;
    private int seats;
    private String description;

    public Car(String brand, String model, int seats, String description) {
        this.brand = brand;
        this.model = model;
        this.seats = seats;
        this.description = description;
    }
}
