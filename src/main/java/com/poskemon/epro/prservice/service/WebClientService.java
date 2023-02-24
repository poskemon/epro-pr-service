package com.poskemon.epro.prservice.service;

import com.poskemon.epro.prservice.domain.dto.PoInfo;
import com.poskemon.epro.prservice.domain.dto.UserInfoDTO;

import java.util.List;

public interface WebClientService {
    List<UserInfoDTO> findUsersByUserNo(List<Long> userNos);

    List<UserInfoDTO> findUsersByRole(int role);

    List<PoInfo> getPoInfoByRfqNo(List<Long> rfqNos);
}
