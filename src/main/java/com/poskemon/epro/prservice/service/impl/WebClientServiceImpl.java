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
public class WebClientServiceImpl implements WebClientService  {

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

    private Mono<UserDTO[]> webclientFindByBuyerNo(List<Long> buyerNoList) {
        String temp = buyerNoList.get(0).toString();
        for (int i = 1; i < buyerNoList.size(); i++) {
            temp += "," + buyerNoList.get(i);
        }
        log.info(temp);

        return loadBalancedWebClientBuilder().filter(lbFunction).build()
                                             .get()
                                             .uri("http://user-service/users/" + temp)
                                             .retrieve()
                                             .bodyToMono(UserDTO[].class);
    }

    private List<?> findAllFallback(Exception e) {
        e.printStackTrace();
        log.error("fallback invoked!");
        return null;
    }
}
