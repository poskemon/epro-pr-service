package com.poskemon.epro.prservice.service;

import com.poskemon.epro.prservice.domain.dto.UserDTO;
import java.util.List;

public interface WebClientService {
    public List<UserDTO> findByBuyerNo(List<Long> buyerNos);
}
