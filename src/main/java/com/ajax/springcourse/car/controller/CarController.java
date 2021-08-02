package com.ajax.springcourse.car.controller;

import com.ajax.springcourse.car.model.Car;
import com.ajax.springcourse.car.model.CarCreateDto;
import com.ajax.springcourse.car.model.CarUpdateDto;
import com.ajax.springcourse.car.service.CarService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping(
        path = "/api/car",
        produces = "application/json"
)
@Slf4j
public class CarController {
    
    private final CarService service;

    @Autowired
    public CarController(CarService service) {
        this.service = service;
    }

    @GetMapping("")
    public ResponseEntity<Iterable<Car>> findAll(){
        Iterable<Car> cars = service.findAll();
        log.info(cars.toString());
        return new ResponseEntity<>(cars, HttpStatus.OK);
    }

    @GetMapping("/search/find-by-model")
    public ResponseEntity<Car> findByModel(@RequestParam String model){
        Car car = service.findByModel(model);
        return new ResponseEntity<>(car,HttpStatus.OK);
    }

    @PostMapping("")
    public ResponseEntity<?> create(@Valid @RequestBody CarCreateDto carDto){
        return new ResponseEntity<>(service.create(carDto),HttpStatus.CREATED);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<?> update(@Valid @RequestBody CarUpdateDto carDto, @PathVariable long id){
        service.update(carDto,id);
        return ResponseEntity.ok().build();
    }
}
