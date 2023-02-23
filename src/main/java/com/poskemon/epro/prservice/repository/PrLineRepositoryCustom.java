package com.poskemon.epro.prservice.repository;

import com.poskemon.epro.prservice.domain.dto.PrHeaderInfo;
import java.util.List;

public interface PrLineRepositoryCustom {

    List<PrHeaderInfo> findPrInfo(Long rfqNo);

}
