package com.example.gracefulstartup;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@EnableAsync
@SpringBootApplication
public class GracefulStartupApplication {

    public static void main(String[] args) {
        SpringApplication.run(GracefulStartupApplication.class, args);
    }

}
