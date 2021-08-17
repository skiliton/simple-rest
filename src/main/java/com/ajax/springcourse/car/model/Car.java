package com.ajax.springcourse.car.model;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Version;

import java.io.Serializable;

@Setter
@Getter
@NoArgsConstructor
@EqualsAndHashCode
public class Car implements Serializable {
    @EqualsAndHashCode.Exclude
    private String id;
    @Version
    @EqualsAndHashCode.Exclude
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
