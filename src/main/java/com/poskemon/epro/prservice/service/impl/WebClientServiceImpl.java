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

    /**
     * 사용자 번호로 조회
     * @param userNos 조회하려는 사용자 번호 리스트
     * @return 조회된 사용자 리스트
     */
    @Override
    @CircuitBreaker(name = "hello4j", fallbackMethod = "findAllFallback")
    public List<UserDTO> findUsersByUserNo(List<Long> userNos) {
        Mono<UserDTO[]> result = webclientFindUsersByUserNo(userNos);
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

    private Mono<UserDTO[]> webclientFindUsersByUserNo(List<Long> userNos) {
        WebClient webClient = WebClient.create();
        StringBuilder temp = new StringBuilder(userNos.get(0).toString());
        for (int i = 1; i < userNos.size(); i++) {
            temp.append(",").append(userNos.get(i));
        }
        log.info(temp.toString());

        return webClient
                .get()
                .uri("http://localhost:8081/users/" + temp)
                .retrieve()
                .bodyToMono(UserDTO[].class);
    }

    /**
     * 권한별 조회
     * @param role 사용부서(1), 바이어(2), 공급사(3), 슈퍼바이어(4)
     * @return 조회된 사용자 리스트
     */
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

    /**
     * 다른 서비스 장애 발생시 실행되는 fallback 메소드
     * @param e error
     * @return null
     */
    private List<?> findAllFallback(Exception e) {
        e.printStackTrace();
        log.error("fallback invoked!");
        return null;
    }
}
