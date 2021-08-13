package com.ajax.springcourse.car.repository;

import com.ajax.springcourse.car.model.Car;
import org.springframework.context.annotation.Profile;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.SetOperations;
import org.springframework.stereotype.Repository;

import javax.annotation.PostConstruct;
import java.util.*;
import java.util.stream.Collectors;

@Repository
@Profile("redis")
public class RedisCarRepository implements CarRepository {

    public static final String HASH_KEY = "cars";
    private final RedisTemplate<String, Object> redisTemplate;

    private HashOperations<String, String, Object> hashOperations;
    private SetOperations<String, Object> setOperations;

    public RedisCarRepository(RedisTemplate<String, Object> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    @PostConstruct
    private void init() {
        hashOperations = redisTemplate.opsForHash();
        setOperations = redisTemplate.opsForSet();
    }

    @Override
    public Optional<Car> findByModel(String model) {
        return hashOperations
                .multiGet(HASH_KEY,getIdsFromModelIndex(model))
                .stream()
                .filter(Car.class::isInstance)
                .map(obj->(Car) obj)
                .findFirst();
    }

    private List<String> getIdsFromModelIndex(String model){
        Set<Object> objects = setOperations.members(getModelIndexKey(model));
        if(objects!=null){
            return objects.stream().map(obj->(String) obj).collect(Collectors.toList());
        }
        return Collections.emptyList();
    }

    @Override
    public Optional<Car> findById(String id) {
        return Optional.ofNullable((Car) hashOperations.get(HASH_KEY, id));
    }

    @Override
    public Car save(Car car) {
        if (car.getId() == null) {
            String id = UUID.randomUUID().toString();
            car.setId(id);
        } else {
            Car oldCar = (Car) hashOperations.get(HASH_KEY, car.getId());
            if (oldCar != null && !oldCar.getModel().equals(car.getModel())) {
                deleteModelIndex(oldCar);
            }
        }
        createModelIndexIfNotExists(car);
        hashOperations.put(HASH_KEY, car.getId(), car);
        return car;
    }

    private void createModelIndexIfNotExists(Car car) {
        String modelIndex = getModelIndexKey(car.getModel());
        setOperations.add(modelIndex, car.getId());
    }

    private String getModelIndexKey(String model) {
        return String.format("cars:model-index:%s", model);
    }

    private void deleteModelIndex(Car car) {
        String modelIndex = getModelIndexKey(car.getModel());
        setOperations.remove(modelIndex, car.getId());
    }

    @Override
    public List<Car> findAll() {
        return hashOperations
                .entries(HASH_KEY)
                .values()
                .stream()
                .filter(Car.class::isInstance)
                .map(obj -> (Car) obj)
                .collect(Collectors.toList());
    }
}
