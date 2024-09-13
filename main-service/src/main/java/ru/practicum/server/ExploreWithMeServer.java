package ru.practicum.server;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = {"ru.practicum.server", "ru.practicum.client"})
public class ExploreWithMeServer {
    public static void main(String[] args) {
        SpringApplication.run(ExploreWithMeServer.class, args);
    }
}
