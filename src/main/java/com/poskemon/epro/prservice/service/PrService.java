package com.poskemon.epro.prservice.service;

import com.poskemon.epro.prservice.domain.dto.PrDetailRes;
import com.poskemon.epro.prservice.domain.dto.PrRequest;
import com.poskemon.epro.prservice.domain.dto.PurchaseUnitReq;
import com.poskemon.epro.prservice.domain.dto.PurchaseUnitRes;
import com.poskemon.epro.prservice.domain.entity.PrHeader;
import com.poskemon.epro.prservice.domain.entity.PrLine;

import java.util.List;

public interface PrService {
    Long prRegist(PrHeader prHeader, List<PrLine> prLines);

    void changeStatus(String prStatus, String prNo);

    PrHeader getPrHeaderDetail(Long prHeaderSeq);

    List<PrDetailRes> getPrLinesDetail(PrHeader prHeader);

    Long modifyPr(PrRequest prRequest);

    void deletePr(Long prHeaderSeq);

    List<PurchaseUnitRes> getAllPrWithParams(PurchaseUnitReq purchaseUnitReq);
}