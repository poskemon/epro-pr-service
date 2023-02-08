package com.poskemon.epro.prservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

@SpringBootApplication
@EnableEurekaClient
public class PrServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(PrServiceApplication.class, args);
    }

}
