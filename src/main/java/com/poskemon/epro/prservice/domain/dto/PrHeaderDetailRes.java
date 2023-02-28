package com.poskemon.epro.prservice.domain.dto;

import com.poskemon.epro.prservice.common.utils.CurrencyUtils;
import com.poskemon.epro.prservice.common.utils.TimesUtils;
import com.poskemon.epro.prservice.domain.entity.PrHeader;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PrHeaderDetailRes {
    private Long prHeaderSeq;
    private String prNo;
    private Long requesterNo;
    private String requesterName;
    private String approvedDate;
    private String prTitle;
    private String prPrice;
    private String prStatus;

    // entity -> dto
    public PrHeaderDetailRes(PrHeader prHeader) {
        this.prHeaderSeq = prHeader.getPrHeaderSeq();
        this.prNo = prHeader.getPrNo();
        this.requesterNo = prHeader.getRequesterNo();
        if (prHeader.getPrApprovedDate() != null) {
            this.approvedDate = TimesUtils.toString(prHeader.getPrApprovedDate(), "yyyy-MM-dd HH:mm:ss");
        }
        this.prTitle = prHeader.getPrTitle();
        this.prPrice = CurrencyUtils.toKRWCurrency(prHeader.getPrPrice());
        this.prStatus = prHeader.getPrStatus();
    }
}
