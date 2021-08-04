package com.ajax.springcourse.car.controller;

import com.ajax.springcourse.car.model.Car;
import com.ajax.springcourse.car.model.dto.CarDto;
import com.ajax.springcourse.car.model.dto.CarUpdateDto;
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

    @GetMapping()
    public List<CarDto> findAll(){
        return service.findAll();
    }

    @GetMapping("/search/find-by-model")
    public CarDto findByModel(@RequestParam String model){
        return service.findByModel(model);
    }

    @PostMapping()
    public CarDto create(@Valid @RequestBody CarDto carDto){
        return service.create(carDto);
    }

    @GetMapping("/{id}")
    public CarDto findById(@PathVariable long id){
        return service.findById(id);
    }

    @PatchMapping("/{id}")
    public CarDto update(@Valid @RequestBody CarUpdateDto carUpdateDto, @PathVariable long id){
        return service.update(carUpdateDto,id);
    }
}
