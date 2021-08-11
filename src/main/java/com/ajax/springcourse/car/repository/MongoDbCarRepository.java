package com.ajax.springcourse.car.repository;

import com.ajax.springcourse.car.model.Car;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

import static org.springframework.data.mongodb.core.query.Criteria.where;
import static org.springframework.data.mongodb.core.query.Query.query;

@Repository
public class MongoDbCarRepository implements CarRepository{

    public static final String COLLECTION_NAME = "cars";
    private final MongoTemplate mongoTemplate;

    @Autowired
    public MongoDbCarRepository(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }

    @Override
    public Optional<Car> findByModel(String model) {
        Query query = query(where("model").is(model));
        return Optional.ofNullable(mongoTemplate.findOne(query,Car.class,COLLECTION_NAME));
    }

    @Override
    public Optional<Car> findById(String id) {
        return Optional.ofNullable(mongoTemplate.findById(id,Car.class,COLLECTION_NAME));
    }

    @Override
    public Car save(Car car) {
        return mongoTemplate.save(car, COLLECTION_NAME);
    }

    @Override
    public List<Car> findAll() {
        return mongoTemplate.findAll(Car.class,COLLECTION_NAME);
    }
}
