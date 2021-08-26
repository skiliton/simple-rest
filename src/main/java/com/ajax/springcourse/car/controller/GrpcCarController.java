package com.ajax.springcourse.car.controller;

import com.ajax.springcourse.car.exception.CarNotFoundException;
import com.ajax.springcourse.car.model.dto.CarCreateDto;
import com.ajax.springcourse.car.model.dto.CarReadDto;
import com.ajax.springcourse.car.model.dto.CarUpdateDto;
import com.ajax.springcourse.car.model.mapper.GrpcCarDtoMapper;
import com.ajax.springcourse.car.observable.GrpcCarStreamObservable;
import com.ajax.springcourse.car.service.CarService;
import com.ajax.springcourse.grpc.Car;
import com.ajax.springcourse.grpc.CarServiceGrpc;
import com.google.protobuf.Empty;
import io.grpc.Status;
import io.grpc.stub.StreamObserver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@Component
@Profile({"redis", "mongo"})
public class GrpcCarController extends CarServiceGrpc.CarServiceImplBase {

    private final CarService carService;

    private final GrpcCarStreamObservable carStreamObservable;

    @Autowired
    public GrpcCarController(CarService carService, GrpcCarStreamObservable carStreamObservable) {
        this.carService = carService;
        this.carStreamObservable = carStreamObservable;
    }

    @Override
    public StreamObserver<Empty> findAll(final StreamObserver<Car.CarReadDto> responseObserver) {
        return new StreamObserver<>() {
            @Override
            public void onNext(Empty value) {
                carService.findAll().stream()
                        .map(GrpcCarDtoMapper::mapToGrpcCarReadDto)
                        .forEach(responseObserver::onNext);
                carStreamObservable.addObserver(responseObserver);
            }

            @Override
            public void onError(Throwable t) {
                carStreamObservable.removeObserver(responseObserver);
                t.printStackTrace();
            }

            @Override
            public void onCompleted() {
                carStreamObservable.removeObserver(responseObserver);
                responseObserver.onCompleted();
            }
        };
    }

    @Override
    public void findByModel(Car.StringParam model, StreamObserver<Car.CarReadDto> responseObserver) {
        carService.findByModel(model.getValue()).stream()
                .map(GrpcCarDtoMapper::mapToGrpcCarReadDto)
                .forEach(responseObserver::onNext);
        responseObserver.onCompleted();
    }

    @Override
    public void create(Car.CarCreateDto request, StreamObserver<Car.CarReadDto> responseObserver) {
        CarCreateDto carCreateDto = GrpcCarDtoMapper.mapToCarCreateDto(request);
        Car.CarReadDto grpcCarReadDto = GrpcCarDtoMapper.mapToGrpcCarReadDto(carService.create(carCreateDto));
        responseObserver.onNext(grpcCarReadDto);
        carStreamObservable.newCarReadDto(grpcCarReadDto);
        responseObserver.onCompleted();
    }

    @Override
    public void findById(Car.StringParam id, StreamObserver<Car.CarReadDto> responseObserver) {
        CarReadDto carReadDto;
        try {
            carReadDto = carService.findById(id.getValue());
        }catch (CarNotFoundException e){
            responseObserver.onError(
                    Status.INVALID_ARGUMENT
                            .withDescription(e.getMessage())
                            .asException());
            return;
        }
        Car.CarReadDto grpcCarReadDto = GrpcCarDtoMapper.mapToGrpcCarReadDto(carReadDto);
        responseObserver.onNext(grpcCarReadDto);
        responseObserver.onCompleted();
    }

    @Override
    public void update(Car.CarUpdateDto request, StreamObserver<Car.CarReadDto> responseObserver) {
        CarUpdateDto carUpdateDto = GrpcCarDtoMapper.mapToCarUpdateDto(request);
        CarReadDto carReadDto;
        try {
            carReadDto = carService.update(carUpdateDto);
        }catch (CarNotFoundException e){
            responseObserver.onError(
                    Status.NOT_FOUND
                            .withDescription(e.getMessage())
                            .asException());
            return;
        }
        Car.CarReadDto grpcCarReadDto  = GrpcCarDtoMapper.mapToGrpcCarReadDto(carReadDto);
        responseObserver.onNext(grpcCarReadDto);
        responseObserver.onCompleted();
    }

    @Override
    public void deleteAll(Empty request, StreamObserver<Empty> responseObserver) {
        carService.deleteAll();
        responseObserver.onNext(Empty.getDefaultInstance());
        responseObserver.onCompleted();
    }
}
