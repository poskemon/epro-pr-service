package com.poskemon.epro.prservice.service;

import com.poskemon.epro.prservice.domain.dto.PrDetailRes;
import com.poskemon.epro.prservice.domain.dto.PrResponse;
import com.poskemon.epro.prservice.domain.entity.PrHeader;
import com.poskemon.epro.prservice.domain.entity.PrLine;

import java.util.List;

public interface PrLineService {
    PrResponse prRegist(PrHeader prHeader, List<PrLine> prLines);

    Integer changeStatus(String prStatus, String prNo);

    PrHeader getPrHeaderDetail(Long prHeaderSeq);

    List<PrDetailRes> getPrDetail(PrHeader prHeader);
}