package com.poskemon.epro.prservice.domain.dto;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PrLineDTO {
    String prStatus;
    Integer rfqNo;
    String elapsedDays;
    String category;
    String prNo;
    Integer prLineSeq;
    String item;
    String itemSpec;
    String unitPrice;
    String uom;
    Integer prQuantity;
    String buyer;
    String requester;
    String noteToBuyer;
}
