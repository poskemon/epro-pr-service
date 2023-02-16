package com.poskemon.epro.prservice.domain.dto;

import com.poskemon.epro.prservice.domain.entity.Item;
import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PrCreateRes {
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
}
