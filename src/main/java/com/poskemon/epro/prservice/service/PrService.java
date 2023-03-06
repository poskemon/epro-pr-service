package com.poskemon.epro.prservice.service;

import com.poskemon.epro.prservice.domain.dto.NeedByDateSearchDTO;
import com.poskemon.epro.prservice.domain.dto.PrDetailRes;
import com.poskemon.epro.prservice.domain.dto.PrHeaderDetailRes;
import com.poskemon.epro.prservice.domain.dto.PrHeaderInfo;
import com.poskemon.epro.prservice.domain.dto.PrRequest;
import com.poskemon.epro.prservice.domain.dto.PrRetrieveReq;
import com.poskemon.epro.prservice.domain.dto.PrRetrieveRes;
import com.poskemon.epro.prservice.domain.dto.PurchaseUnitReq;
import com.poskemon.epro.prservice.domain.dto.PurchaseUnitRes;
import com.poskemon.epro.prservice.domain.entity.PrHeader;
import com.poskemon.epro.prservice.domain.entity.PrLine;
import java.io.IOException;
import java.util.List;

public interface PrService {
    Long prRegist(PrHeader prHeader, List<PrLine> prLines);

    void changeStatus(String prStatus, String prNo);

    PrHeaderDetailRes getPrHeaderDetail(Long prHeaderSeq);

    PrHeader getPrHeader(Long prHeaderSeq);

    List<PrDetailRes> getPrLinesDetail(PrHeader prHeader);

    Long modifyPr(PrRequest prRequest);

    void deletePr(Long prHeaderSeq);

    List<PurchaseUnitRes> getAllPrWithParams(PurchaseUnitReq purchaseUnitReq);

    List<PrRetrieveRes> getAllPr(PrRetrieveReq prRetrieveReq);

    void setRfqNo(String message) throws IOException;

    List<NeedByDateSearchDTO> getNeedByDateByRfqNo(List<Long> rfqNos);

    List<Long> retrieveItemInfoByRfqNo(List<Long> rfqNos);

    List<PrHeaderInfo> retrievePrInfoByRfqNo(Long rfqNo);

    List<PrHeader> findPrHeader();
}