package com.poskemon.epro.prservice.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PoInfo {
    private Long rfqNo;
    private Long poNo;
    private String poPrice;
}
