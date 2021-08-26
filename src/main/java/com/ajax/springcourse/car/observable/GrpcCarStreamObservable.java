package com.ajax.springcourse.car.observable;

import com.ajax.springcourse.car.model.dto.CarReadDto;
import com.ajax.springcourse.grpc.Car;
import io.grpc.stub.StreamObserver;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
@Slf4j
public class GrpcCarStreamObservable {
    private final List<StreamObserver<Car.CarReadDto>> observers = new ArrayList<>();

    public void addObserver(StreamObserver<Car.CarReadDto> observer){
        log.info("Adding observer "+observer.toString());
        observers.add(observer);
    }

    public void removeObserver(StreamObserver<Car.CarReadDto> observer){
        log.info("Removing observer "+observer.toString());
        observers.remove(observer);
    }

    public void newCarReadDto(Car.CarReadDto carReadDto){
        log.info("Pushing new car with id="+carReadDto.getId());
        observers.forEach(observer -> observer.onNext(carReadDto));
    }
}
