package com.poskemon.epro.prservice.domain.dto;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 구매신청현황 조회 조건
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PrRetrieveReq {
    private String prNo;
    private String prTitle;
    private Long itemNo;
    private Long RequesterNo;
    private Long buyerNo;
    private String prCreationDate;
    private String prStatus;
}