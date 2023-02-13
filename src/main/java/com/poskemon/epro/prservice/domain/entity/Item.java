package com.poskemon.epro.prservice.domain.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "item")
public class Item {
    @Id
    @Column(name = "item_no")
    private Long itemNo; // 아이템 번호

    private String category; // 카테고리

    @Column(name = "item_description")
    private String itemDescription; // 아이템 명

    private String spec; // 사양

    private String uom; // 측정 단위
}
