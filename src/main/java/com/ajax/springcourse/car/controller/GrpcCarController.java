package com.ajax.springcourse.car.controller;

import com.ajax.springcourse.car.model.dto.CarCreateDto;
import com.ajax.springcourse.car.model.dto.CarReadDto;
import com.ajax.springcourse.car.model.dto.CarUpdateDto;
import com.ajax.springcourse.car.model.mapper.GrpcCarDtoMapper;
import com.ajax.springcourse.car.service.CarService;
import com.ajax.springcourse.grpc.Car;
import com.ajax.springcourse.grpc.CarServiceGrpc;
import com.google.protobuf.Empty;
import io.grpc.stub.StreamObserver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Profile({"redis", "mongo"})
public class GrpcCarController extends CarServiceGrpc.CarServiceImplBase {

    private final CarService carService;

    @Autowired
    public GrpcCarController(CarService carService) {
        this.carService = carService;
    }

    @Override
    public void findAll(Empty request, StreamObserver<Car.CarReadDtoList> responseObserver) {
        List<CarReadDto> carReadDtos = carService.findAll();
        Car.CarReadDtoList grpcCarDtoList = GrpcCarDtoMapper.mapToGrpcCarReadDto(carReadDtos);
        responseObserver.onNext(grpcCarDtoList);
        responseObserver.onCompleted();
    }

    @Override
    public void findByModel(Car.StringParam model, StreamObserver<Car.CarReadDtoList> responseObserver) {
        List<CarReadDto> cars = carService.findByModel(model.getValue());
        Car.CarReadDtoList grpcCarDtoList = GrpcCarDtoMapper.mapToGrpcCarReadDto(cars);
        responseObserver.onNext(grpcCarDtoList);
        responseObserver.onCompleted();
    }

    @Override
    public void create(Car.CarCreateDto grpcCarCreateDto, StreamObserver<Car.CarReadDto> responseObserver) {
        CarCreateDto carCreateDto = GrpcCarDtoMapper.mapToCarCreateDto(grpcCarCreateDto);
        Car.CarReadDto grpcCarReadDto = GrpcCarDtoMapper.mapToGrpcCarReadDto(carService.create(carCreateDto));
        responseObserver.onNext(grpcCarReadDto);
        responseObserver.onCompleted();
    }

    @Override
    public void findById(Car.StringParam id, StreamObserver<Car.CarReadDto> responseObserver) {
        CarReadDto carReadDto = carService.findById(id.getValue());
        Car.CarReadDto grpcCarReadDto = GrpcCarDtoMapper.mapToGrpcCarReadDto(carReadDto);
        responseObserver.onNext(grpcCarReadDto);
        responseObserver.onCompleted();
    }

    @Override
    public void update(Car.CarUpdateDto grpcCarUpdateDto, StreamObserver<Car.CarReadDto> responseObserver) {
        CarUpdateDto carUpdateDto = GrpcCarDtoMapper.mapToCarUpdateDto(grpcCarUpdateDto);
        Car.CarReadDto grpcCarReadDto  = GrpcCarDtoMapper.mapToGrpcCarReadDto(carService.update(carUpdateDto));
        responseObserver.onNext(grpcCarReadDto);
        responseObserver.onCompleted();
    }
}
