package com.poskemon.epro.prservice.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class ItemInfoDb {

    private Long itemNo;

    private String itemDescription;

    private String uom;

    private Long unitPrice;

    private Long prQuantity;

}
