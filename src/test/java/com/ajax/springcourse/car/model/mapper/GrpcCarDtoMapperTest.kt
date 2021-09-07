package com.ajax.springcourse.car.model.mapper

import com.ajax.springcourse.car.model.dto.CarReadDto
import com.ajax.springcourse.car.model.mapper.GrpcCarDtoMapper.*
import com.ajax.springcourse.grpc.Car
import org.jeasy.random.EasyRandom
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.Assertions.*

class GrpcCarDtoMapperTest{

    private val random = EasyRandom()

    @Test
    fun shouldMapFromCarReadDtoToGrpcCarReadDto(){
        //GIVEN
        val carReadDto = random.nextObject(CarReadDto::class.java)
        //WHEN
        val result = mapToGrpcCarReadDto(carReadDto)
        //THEN
        assertEquals(carReadDto.id,result.id)
        assertEquals(carReadDto.brand,result.brand)
        assertEquals(carReadDto.model,result.model)
        assertEquals(carReadDto.seats,result.seats)
        assertEquals(carReadDto.description,result.description)
    }

    @Test
    fun shouldMapFromGrpcCarCreateDtoToCarCreateDto(){
        //GIVEN
        val grpcCarCreateDto = random.nextObject(Car.CarCreateDto::class.java)
        //WHEN
        val result = mapToCarCreateDto(grpcCarCreateDto)
        //THEN
        assertEquals(grpcCarCreateDto.brand,result.brand)
        assertEquals(grpcCarCreateDto.model,result.model)
        assertEquals(grpcCarCreateDto.seats,result.seats)
        assertEquals(grpcCarCreateDto.description,result.description)
    }

    @Test
    fun shouldMapFromGrpcCarUpdateDtoToCarUpdateDto(){
        //GIVEN
        val grpcCarUpdateDto = random.nextObject(Car.CarUpdateDto::class.java)
        //WHEN
        val result = mapToCarUpdateDto(grpcCarUpdateDto)
        //THEN
        assertEquals(grpcCarUpdateDto.id,result.id)
        assertEquals(grpcCarUpdateDto.description,result.description)
    }
}