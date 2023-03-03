package com.poskemon.epro.prservice.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PrApprovalParam {
    private Long prHeaderSeq;
    private String prStatus;
}
