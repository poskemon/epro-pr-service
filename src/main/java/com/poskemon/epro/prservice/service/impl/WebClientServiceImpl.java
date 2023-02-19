package com.poskemon.epro.prservice.service.impl;

import com.poskemon.epro.prservice.domain.dto.UserDTO;
import com.poskemon.epro.prservice.service.WebClientService;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.client.loadbalancer.reactive.ReactorLoadBalancerExchangeFilterFunction;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Slf4j
@Service
@RequiredArgsConstructor
public class WebClientServiceImpl implements WebClientService {

    private final ReactorLoadBalancerExchangeFilterFunction lbFunction;

    private WebClient.Builder loadBalancedWebClientBuilder() {
        return WebClient.builder();
    }

    @Override
    @CircuitBreaker(name = "hello4j", fallbackMethod = "findAllFallback")
    public List<UserDTO> findByBuyerNo(List<Long> buyerNoList) {
        Mono<UserDTO[]> result = webclientFindByBuyerNo(buyerNoList);
        return Arrays.stream(result.block()).collect(Collectors.toList());
    }

    // 게이트웨이에서 uri에서 lb를 사용할 경우 로드밸런싱 추가해야 함!
//    private Mono<UserDTO[]> webclientFindByBuyerNo(List<Long> buyerNoList) {
//        String temp = buyerNoList.get(0).toString();
//        for (int i = 1; i < buyerNoList.size(); i++) {
//            temp += "," + buyerNoList.get(i);
//        }
//        log.info(temp);
//
//        return loadBalancedWebClientBuilder().filter(lbFunction).build()
//                .get()
//                .uri("http://user-service/users/" + temp)
//                .retrieve()
//                .bodyToMono(UserDTO[].class);
//    }

    private Mono<UserDTO[]> webclientFindByBuyerNo(List<Long> buyerNoList) {
        WebClient webClient = WebClient.create();
        StringBuilder temp = new StringBuilder(buyerNoList.get(0).toString());
        for (int i = 1; i < buyerNoList.size(); i++) {
            temp.append(",").append(buyerNoList.get(i));
        }
        log.info(temp.toString());

        return webClient
                .get()
                .uri("http://localhost:8081/users/" + temp)
                .retrieve()
                .bodyToMono(UserDTO[].class);
    }

    @Override
    @CircuitBreaker(name = "hello4j", fallbackMethod = "findAllFallback")
    public List<UserDTO> findUsersByRole(int role) {
        Mono<UserDTO[]> result = webclientFindUsersByRole(role);
        return Arrays.stream(result.block()).collect(Collectors.toList());
    }

    private Mono<UserDTO[]> webclientFindUsersByRole(int role) {
        WebClient webClient = WebClient.create();
        return webClient
                .get()
                .uri("http://localhost:8081/role/" + role)
                .retrieve()
                .bodyToMono(UserDTO[].class);
    }

    private List<?> findAllFallback(Exception e) {
        e.printStackTrace();
        log.error("fallback invoked!");
        return null;
    }
}
