package com.ajax.springcourse.car.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Version;
import org.springframework.data.redis.core.RedisHash;

import java.io.Serializable;

@Setter
@Getter
@RedisHash("car")
@NoArgsConstructor
public class Car implements Serializable {
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
