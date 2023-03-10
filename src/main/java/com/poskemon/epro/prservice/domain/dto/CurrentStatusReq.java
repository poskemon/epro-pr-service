package com.poskemon.epro.prservice.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CurrentStatusReq {
    private Long poNo;
    private Long rfqNo;
}
