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
    private Long prLineSeq; // 기본키
    private String prTitle;
    private String prStatus;
    private Long requesterNo;
    private String requesterName;
    private Long buyerNo;
    private String buyerName;
    private String prPrice;
    private String prCreationDate;
    private String prApprovedDate;
    private Long rfqNo;
    private Long poNo; // po번호
    private String poPrice; // 계약금액

    public PrRetrieveRes(PrLine prLine) {
        this.prLineSeq = prLine.getPrLineSeq();
        this.prTitle = prLine.getPrHeader().getPrTitle();
        this.prStatus = prLine.getPrHeader().getPrStatus();
        this.requesterNo = prLine.getPrHeader().getRequesterNo();
        this.buyerNo = prLine.getBuyerNo();
        this.rfqNo = prLine.getRfqNo();
        this.prPrice = CurrencyUtils.toKRWCurrency(prLine.getPrHeader().getPrPrice());
        this.prCreationDate = TimesUtils.toString(prLine.getPrHeader().getPrCreationDate(), "yyyy-MM-dd HH:mm:ss");
        if(prLine.getPrHeader().getPrApprovedDate() != null) {
            this.prApprovedDate = TimesUtils.toString(prLine.getPrHeader().getPrApprovedDate(), "yyyy-MM-dd HH:mm:ss");
        }
    }
}
