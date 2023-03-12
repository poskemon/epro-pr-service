package com.poskemon.epro.prservice.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@Getter
@NoArgsConstructor
public class ItemInfo {

    private Integer item_id; // item_no

    private String item; // item_description

    private String uom; // uom

    private Integer unit_price; // unit_price

    private Integer mat_bpa_agree_qt; // pr_quantity

    private String description;

}
