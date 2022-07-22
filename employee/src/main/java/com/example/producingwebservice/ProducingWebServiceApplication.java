package com.example.producingwebservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

@SpringBootApplication
@EnableEurekaClient
public class ProducingWebServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(ProducingWebServiceApplication.class, args);

    }
}
