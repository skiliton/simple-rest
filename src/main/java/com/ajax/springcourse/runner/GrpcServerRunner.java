package com.ajax.springcourse.runner;

import io.grpc.Server;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class GrpcServerRunner implements CommandLineRunner {

    private final Server server;

    @Autowired
    public GrpcServerRunner(Server server) {
        this.server = server;
    }

    @Override
    public void run(String... args) throws Exception {
        log.info("gRPC server starting...");
        server.start();
        log.info("gRPC server started");
    }
}
