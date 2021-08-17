package com.ajax.springcourse.car.repository;

import com.ajax.springcourse.car.model.Car;
import org.springframework.context.annotation.Profile;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.SetOperations;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Repository;

import javax.annotation.PostConstruct;
import java.util.*;
import java.util.stream.Collectors;

@Repository
@Profile("redis")
public class RedisCarRepository implements CarRepository {

    public static final String CAR_HASH_PREFIX = "cars";
    public static final String ID = "id";
    public static final String BRAND = "brand";
    public static final String MODEL = "model";
    public static final String SEATS = "seats";
    public static final String DESCRIPTION = "description";
    private final RedisTemplate<String, String> redisTemplate;

    private HashOperations<String, String, String> hashOperations;
    private SetOperations<String, String> setOperations;
    private ValueOperations<String, String> keyOperations;

    public RedisCarRepository(RedisTemplate<String, String> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    @PostConstruct
    private void init() {
        hashOperations = redisTemplate.opsForHash();
        setOperations = redisTemplate.opsForSet();
        keyOperations = redisTemplate.opsForValue();
    }

    @Override
    public List<Car> findByModel(String model) {
        return getHashesFromModel(model)
                .stream()
                .map(key -> hashOperations.entries(key))
                .map(this::entriesToCar)
                .collect(Collectors.toList());
    }



    @Override
    public Optional<Car> findById(String id) {
        Map<String,String> entries = hashOperations.entries(getCarHashKey(id));
        if(entries.isEmpty()){
            return Optional.empty();
        }
        return Optional.of(entriesToCar(entries));
    }

    @Override
    public Car save(Car car) {
        if (car.getId() == null) {
            String id = UUID.randomUUID().toString();
            car.setId(id);
        } else {
            Car oldCar = entriesToCar(hashOperations.entries(getCarHashKey(car.getId())));
            if (!oldCar.getModel().equals(car.getModel())) {
                deleteModelIndex(oldCar);
            }
        }
        createModelIndexIfNotExists(car);
        hashOperations.putAll(getCarHashKey(car.getId()),carToEntries(car));
        return car;
    }

    @Override
    public List<Car> findAll() {
        return getAllHashKeys()
                .stream()
                .map(key -> hashOperations.entries(key))
                .map(this::entriesToCar)
                .collect(Collectors.toList());
    }

    private Set<String> getHashesFromModel(String model) {
        Set<String> indexes = setOperations.members(getModelIndexKey(model));
        if (indexes != null) {
            return indexes;
        }
        return Collections.emptySet();
    }

    private Set<String> getAllHashKeys(){
        Set<String> keys =  keyOperations.getOperations().keys("cars:*");
        if (keys != null) {
            return keys;
        }
        return Collections.emptySet();
    }

    private void createModelIndexIfNotExists(Car car) {
        String modelIndex = getModelIndexKey(car.getModel());
        setOperations.add(modelIndex, getCarHashKey(car.getId()));
    }

    private String getModelIndexKey(String model) {
        return String.format("indexes:cars:model:%s", model);
    }

    private String getCarHashKey(String id) {
        return String.format("cars:%s", id);
    }

    private void deleteModelIndex(Car car) {
        String modelIndex = getModelIndexKey(car.getModel());
        setOperations.remove(modelIndex, getCarHashKey(car.getId()));
    }

    private Car entriesToCar(Map<String,String> entries){
        Car car = new Car();
        car.setId(entries.getOrDefault(ID,""));
        car.setBrand(entries.getOrDefault(BRAND,""));
        car.setModel(entries.getOrDefault(MODEL,""));
        car.setSeats(Integer.parseInt(entries.getOrDefault(SEATS,"0")));
        car.setDescription(entries.getOrDefault(DESCRIPTION,""));
        return car;
    }

    private Map<String,String> carToEntries(Car car){
        Map<String,String> entries = new HashMap<>();
        entries.put(ID,car.getId());
        entries.put(BRAND,car.getBrand());
        entries.put(MODEL,car.getModel());
        entries.put(SEATS,String.valueOf(car.getSeats()));
        entries.put(DESCRIPTION,car.getDescription());
        return entries;
    }

}
