package com.ajax.springcourse.car.migration;

import com.ajax.springcourse.car.model.Car;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Collections;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class MigrationResult {
    private List<Car> extraCars;
    private List<Car> carsNotFound;

    public MigrationResult() {
        extraCars = Collections.emptyList();
        carsNotFound= Collections.emptyList();
    }
}
