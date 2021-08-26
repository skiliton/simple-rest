package com.ajax.springcourse.config;

import io.grpc.BindableService;
import io.grpc.Server;
import io.grpc.ServerBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;
import java.util.stream.Collectors;

@Configuration
public class GrpcConfig {

    @Bean
    public Server grpcServer(List<BindableService> services) {
        return ServerBuilder
                .forPort(50051)
                .addServices(
                    services.stream()
                        .map(BindableService::bindService)
                        .collect(Collectors.toList()))
                .build();
    }
}
