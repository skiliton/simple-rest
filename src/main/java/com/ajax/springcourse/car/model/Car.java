package com.ajax.springcourse.car.model;

import lombok.Data;

import javax.persistence.*;

@Entity
@Table(name = "cars")
@Data
public class Car {
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private long id;
    private String brand;
    private String model;
    private int seats;
    private String description;
    protected Car() {}

    public Car(String brand, String model, int seats, String description) {
        this.brand = brand;
        this.model = model;
        this.seats = seats;
        this.description = description;
    }
}
