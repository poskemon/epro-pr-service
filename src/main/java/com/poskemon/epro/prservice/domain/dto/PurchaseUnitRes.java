package com.poskemon.epro.prservice.domain.dto;

import com.poskemon.epro.prservice.domain.entity.PrLine;

import com.poskemon.epro.prservice.domain.entity.Item;
import java.time.LocalDate;
import lombok.*;

/**
 * 구매단위편성 조회 결과
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PurchaseUnitRes {
    private String prStatus; // 진행상태 (승인요청, 승인완료)
    private Long rfqNo;
    private Long elapsedDate; // 현재날짜 - 승인날짜
    private String category;
    private String itemDescription;
    private String spec;
    private String uom;
    private String prNo;
    private Long prLine;
    private Long unitPrice;
    private Long prQuantity;
    private Long buyerNo;
    private String buyerName;
    private Long requesterNo;
    private String requesterName;
    private String noteToBuyer;
    private LocalDate needByDate; // 납기일

    // entity -> dto
    public PurchaseUnitRes(PrLine prLine) {
        this.prStatus = prLine.getPrHeader().getPrStatus();
        this.rfqNo = prLine.getRfqNo();
        this.category = prLine.getItem().getCategory();
        this.itemDescription = prLine.getItem().getItemDescription();
        this.spec = prLine.getItem().getSpec();
        this.uom = prLine.getItem().getUom();
        this.prNo = prLine.getPrHeader().getPrNo();
        this.prLine = prLine.getPrLine();
        this.unitPrice = prLine.getUnitPrice();
        this.prQuantity = prLine.getPrQuantity();
        this.buyerNo = prLine.getBuyerNo();
        this.requesterNo = prLine.getPrHeader().getRequesterNo();
        this.noteToBuyer = prLine.getNoteToBuyer();
        this.needByDate = prLine.getNeedByDate();
    }
}
