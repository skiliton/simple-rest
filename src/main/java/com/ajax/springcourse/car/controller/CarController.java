package com.ajax.springcourse.car.controller;

import com.ajax.springcourse.car.model.Car;
import com.ajax.springcourse.car.model.dto.CarCreateDto;
import com.ajax.springcourse.car.model.dto.CarReadDto;
import com.ajax.springcourse.car.model.dto.CarUpdateDto;
import com.ajax.springcourse.car.service.CarService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
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
    public List<CarReadDto> findAll(){
        return service.findAll();
    }

    @GetMapping("/search/find-by-model")
    public CarReadDto findByModel(@RequestParam String model){
        return service.findByModel(model);
    }

    @PostMapping()
    public CarReadDto create(@Valid @RequestBody CarCreateDto carCreateDto){
        return service.create(carCreateDto);
    }

    @GetMapping("/{id}")
    public CarReadDto findById(@PathVariable String id){
        return service.findById(id);
    }

    @PatchMapping()
    public CarReadDto update(@Valid @RequestBody CarUpdateDto carUpdateDto){
        return service.update(carUpdateDto);
    }
}
