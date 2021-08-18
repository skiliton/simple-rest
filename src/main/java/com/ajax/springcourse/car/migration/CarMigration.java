package com.ajax.springcourse.car.migration;

import com.ajax.springcourse.car.model.Car;

import java.util.List;

public interface CarMigration {
    MigrationResult migrate();
}
