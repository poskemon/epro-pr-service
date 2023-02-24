package com.poskemon.epro.prservice.service.impl;

import com.poskemon.epro.prservice.domain.dto.PoInfo;
import com.poskemon.epro.prservice.domain.dto.UserInfoDTO;
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
     *
     * @param userNos 조회하려는 사용자 번호 리스트
     * @return 조회된 사용자 리스트
     */
    @Override
    @CircuitBreaker(name = "hello4j", fallbackMethod = "findAllFallback")
    public List<UserInfoDTO> findUsersByUserNo(List<Long> userNos) {
        Mono<UserInfoDTO[]> result = webclientFindUsersByUserNo(userNos);
        return Arrays.stream(result.block()).collect(Collectors.toList());
    }

    private Mono<UserInfoDTO[]> webclientFindUsersByUserNo(List<Long> userNos) {
        StringBuilder temp = new StringBuilder(userNos.get(0).toString());
        for (int i = 1; i < userNos.size(); i++) {
            temp.append(",").append(userNos.get(i));
        }
        log.info(temp.toString());

        return loadBalancedWebClientBuilder().filter(lbFunction).build()
                                             .get()
                                             .uri("http://user-service/users/" + temp)
                                             .retrieve()
                                             .bodyToMono(UserInfoDTO[].class);
    }

    /**
     * 권한별 조회
     *
     * @param role 사용부서(1), 바이어(2), 공급사(3), 슈퍼바이어(4)
     * @return 조회된 사용자 리스트
     */
    @Override
    @CircuitBreaker(name = "hello4j", fallbackMethod = "findAllFallback")
    public List<UserInfoDTO> findUsersByRole(int role) {
        Mono<UserInfoDTO[]> result = webclientFindUsersByRole(role);
        return Arrays.stream(result.block()).collect(Collectors.toList());
    }

    private Mono<UserInfoDTO[]> webclientFindUsersByRole(int role) {
        return loadBalancedWebClientBuilder().filter(lbFunction).build()
                                             .get()
                                             .uri("http://user-service/role/" + role)
                                             .retrieve()
                                             .bodyToMono(UserInfoDTO[].class);
    }

    /**
     * rfqNo로 poNo, poPrice 조회
     *
     * @param rfqNos rfqNo 리스트
     * @return 조회된 poInfo 리스트
     */
    @Override
    @CircuitBreaker(name = "hello4j", fallbackMethod = "findAllFallback")
    public List<PoInfo> getPoInfoByRfqNo(List<Long> rfqNos) {
        Mono<PoInfo[]> result = webclientGetPoInfoByRfqNo(rfqNos);
        return Arrays.stream(result.block()).collect(Collectors.toList());
    }

    private Mono<PoInfo[]> webclientGetPoInfoByRfqNo(List<Long> rfqNos) {
        StringBuilder temp = new StringBuilder(rfqNos.get(0).toString());
        for (int i = 1; i < rfqNos.size(); i++) {
            temp.append(",").append(rfqNos.get(i));
        }
        return loadBalancedWebClientBuilder().filter(lbFunction).build()
                                             .get()
                                             .uri("http://localhost:8081/po/" + temp)
                                             .retrieve()
                                             .bodyToMono(PoInfo[].class);
    }

    /**
     * 다른 서비스 장애 발생시 실행되는 fallback 메소드
     *
     * @param e error
     * @return null
     */
    private List<?> findAllFallback(Exception e) {
        e.printStackTrace();
        log.error("fallback invoked!");
        return null;
    }
}
