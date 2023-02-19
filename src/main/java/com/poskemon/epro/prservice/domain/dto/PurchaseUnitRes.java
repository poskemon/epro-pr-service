package com.poskemon.epro.prservice.domain.dto;

import java.sql.Date;

import com.poskemon.epro.prservice.domain.entity.Item;
import lombok.*;

/**
 * 구매단위편성 조회 결과
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PurchaseUnitRes {
    // TODO - 동현님한테 물어보기
    private String prStatus; // 진행상태 (승인요청, 승인완료)
    private Long rfqNo;
    private String elapsedDays; // 경과일 (현재날짜 - 승인요청일)
    private Item item;
    private String prNo;
    private Integer prLine;
    private Integer prQuantity;
    private Long buyerNo;
    private String buyerName;
    private String requesterNo;
    private String requesterName;
    private String noteToBuyer;
    private Date needByDate; // 납기일
}
