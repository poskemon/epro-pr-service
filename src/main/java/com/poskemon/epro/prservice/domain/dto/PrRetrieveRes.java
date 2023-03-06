package com.poskemon.epro.prservice.domain.dto;

import com.poskemon.epro.prservice.common.utils.CurrencyUtils;
import com.poskemon.epro.prservice.common.utils.TimesUtils;
import com.poskemon.epro.prservice.domain.entity.PrLine;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 구매신청현황 조회 결과
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PrRetrieveRes {
    private Long prHeaderSeq; // prHeader 기본키
    private Long prLineSeq; // prLine 기본키
    private String prNo;
    private String prTitle;
    private String prStatus;
    private Long requesterNo;
    private String requesterName;
    private Long buyerNo;
    private String buyerName;
    private Long prLinePrice;
    private String prCreationDate;
    private String prApprovedDate;
    private Long rfqNo;
    private Long poNo; // po번호
    private Long poPrice; // 계약금액
    private String noteToBuyer;

    public PrRetrieveRes(PrLine prLine) {
        this.prHeaderSeq = prLine.getPrHeader().getPrHeaderSeq();
        this.prLineSeq = prLine.getPrLineSeq();
        this.prNo = prLine.getPrHeader().getPrNo();
        this.prTitle = prLine.getPrHeader().getPrTitle();
        this.prStatus = prLine.getPrHeader().getPrStatus();
        this.requesterNo = prLine.getPrHeader().getRequesterNo();
        this.buyerNo = prLine.getBuyerNo();
        this.rfqNo = prLine.getRfqNo();
        this.prLinePrice = prLine.getPrLinePrice();
        this.prCreationDate = TimesUtils.toString(prLine.getPrHeader().getPrCreationDate(), "yyyy-MM-dd HH:mm:ss");
        if(prLine.getPrHeader().getPrApprovedDate() != null) {
            this.prApprovedDate = TimesUtils.toString(prLine.getPrHeader().getPrApprovedDate(), "yyyy-MM-dd HH:mm:ss");
        }
        this.noteToBuyer = prLine.getNoteToBuyer();
    }
}
