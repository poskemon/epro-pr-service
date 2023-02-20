package com.poskemon.epro.prservice.service;

import com.poskemon.epro.prservice.domain.dto.UserDTO;

import java.util.List;

public interface WebClientService {
    List<UserDTO> findUsersByUserNo(List<Long> userNos);

    List<UserDTO> findUsersByRole(int role);
}
