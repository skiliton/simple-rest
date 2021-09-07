package com.ajax.springcourse.car.observable

import com.ajax.springcourse.grpc.Car
import io.grpc.stub.StreamObserver
import io.nats.client.Connection
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import org.springframework.util.SerializationUtils
import javax.annotation.PostConstruct

@Component
class GrpcCarStreamObservable @Autowired constructor(val connection: Connection) {

    companion object {
        const val SUBJECT = "carReadDto.create"
    }

    private val logger = LoggerFactory.getLogger(this::class.java)

    private val observers: MutableList<StreamObserver<Car.CarReadDto>> = ArrayList()

    @PostConstruct
    fun init() {
        val dispatcher = connection.createDispatcher { msg ->
            val carReadDto = SerializationUtils.deserialize(msg.data) as Car.CarReadDto
            logger.info("Got car with id=${carReadDto.id}")
            observers.forEach { it.onNext(carReadDto) }
        }
        dispatcher.subscribe(SUBJECT)
    }

    fun addObserver(observer: StreamObserver<Car.CarReadDto>) {
        observers.add(observer)
    }

    fun removeObserver(observer: StreamObserver<Car.CarReadDto>) {
        observers.remove(observer)
    }

    fun newCarReadDto(carReadDto: Car.CarReadDto) {
        logger.info("Sending car with id=${carReadDto.id}")
        connection.publish(SUBJECT, SerializationUtils.serialize(carReadDto))
    }
}