package com.poskemon.epro.prservice.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 게이트 웨이가 애플리케이션 상태를 확인하는 용도의 클래스
 */
@RestController
public class HealthCheck {

    @GetMapping("/")
    public String healthCheck() {

        return "The pr-service is up and running ...";
    }
}
