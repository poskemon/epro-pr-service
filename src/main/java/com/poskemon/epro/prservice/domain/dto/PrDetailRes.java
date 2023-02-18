package com.poskemon.epro.prservice.domain.dto;

import com.poskemon.epro.prservice.domain.entity.Item;

import java.time.LocalDate;

import com.poskemon.epro.prservice.domain.entity.PrLine;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PrDetailRes {
    private Long prLineSeq;
    private Item item;
    private Long rfqNo;
    private Long unitPrice;
    private Long prQuantity;
    private Long prLinePrice;
    private String noteToBuyer;
    private LocalDate needByDate;
    private Long prLine;
    private Long buyerNo;
    private String buyerName;

    // entity -> dto
    public PrDetailRes(PrLine prLine) {
        this.prLineSeq = prLine.getPrLineSeq();
        this.item = prLine.getItem();
        this.rfqNo = prLine.getRfqNo();
        this.unitPrice = prLine.getUnitPrice();
        this.prQuantity = prLine.getPrQuantity();
        this.prLineSeq = prLine.getPrLineSeq();
        this.noteToBuyer = prLine.getNoteToBuyer();
        this.needByDate = prLine.getNeedByDate();
        this.prLine = prLine.getPrLine();
        this.buyerNo = prLine.getBuyerNo();
    }
}
