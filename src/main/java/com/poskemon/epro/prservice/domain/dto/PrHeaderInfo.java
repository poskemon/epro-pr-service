package com.poskemon.epro.prservice.domain.dto;

import com.poskemon.epro.prservice.common.utils.TimesUtils;
import java.time.LocalDate;
import java.time.LocalDateTime;
import lombok.Data;

/**
 * Rfq 에 해당하는 PR 정보를 담아서 Po-service 에 반환하는 DTO
 *
 * @author coalong
 */
@Data
public class PrHeaderInfo {

    // MEMO: pr_header 조회
    private Long requesterNo;

    private String prNo;

    private Long prHeaderSeq;

    private LocalDateTime prCreationDateDb;
    private String prCreationDate;

    // MEMO: pr_line 조회
    private Long rfqNo;

    private Long price;

    private LocalDate needByDate;

    public PrHeaderInfo(Long requesterNo, String prNo, Long prHeaderSeq, LocalDateTime prCreationDateDb, Long rfqNo, Long price, LocalDate needByDate) {
        this.requesterNo = requesterNo;
        this.prNo = prNo;
        this.prHeaderSeq = prHeaderSeq;
        this.prCreationDateDb = prCreationDateDb;
        this.prCreationDate = TimesUtils.toString(prCreationDateDb);
        this.rfqNo = rfqNo;
        this.price = price;
        this.needByDate = needByDate;
    }

}
