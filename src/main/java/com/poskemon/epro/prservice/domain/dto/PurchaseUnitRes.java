package com.poskemon.epro.prservice.domain.dto;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PurchaseUnitRes {
    String prNo;
    String prTitle;
    String requester;
    String buyer;
    String item;
    String itemSpec;
    String prStatus;
    String category;
    Long rfqNo;
}
