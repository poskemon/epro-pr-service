package com.poskemon.epro.prservice.domain.dto;

import lombok.*;

/**
 * 구매단위편성 조회 조건
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PurchaseUnitReq {
    private String prNo; // pr 번호
    private String prTitle; // pr 건명
    private Long requesterNo; // 구매 신청자
    private Long buyerNo; // 바이어 번호
    private String itemDescription; // 아이템 명
    private String spec; // 사양
    private String prStatus; // 진행상태
    private String category; // 카테고리
    private Long rfqNo;
}
