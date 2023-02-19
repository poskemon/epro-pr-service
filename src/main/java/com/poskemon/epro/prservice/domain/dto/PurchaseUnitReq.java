package com.poskemon.epro.prservice.domain.dto;

import lombok.*;
import org.aspectj.lang.annotation.RequiredTypes;

/**
 * 구매단위편성 조회 조건
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PurchaseUnitReq {
    // TODO - 동현님한테 물어보기
    private String prNo; // pr 번호
    private String prTitle; // pr 건명
    private Long requesterNo; // 구매 신청자
    private Long buyerNo; // 바이어 번호
    private String itemDescription;
    private String itemSpec;
    private String prStatus;
    private String category;
    private Long rfqNo; // ??
}
