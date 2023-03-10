package com.poskemon.epro.prservice.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CurrentStatusRes {
    private Long rfqNo;
    private String rfqStatus;
    private String rfqCreationDate;
    private String bidPostingDate;
    private Long poSeq; // poSeq
    private String poStatus;
    private String poCreationDate;
    private String poApprovedDate;
}
