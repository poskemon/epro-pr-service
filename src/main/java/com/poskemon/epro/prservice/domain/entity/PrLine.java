package com.poskemon.epro.prservice.domain.entity;

import java.time.LocalDate;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 구매 신청(pr_line) 엔티티
 */
@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "pr_line")
@SequenceGenerator(
    name = "pr_line_seq_generator",
    sequenceName = "pr_line_seq", // 매핑할 데이터베이스 시퀀스 이름
    initialValue = 1000, allocationSize = 1)
public class PrLine {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE,
                    generator = "pr_line_seq_generator")
    @Column(name = "pr_line_seq")
    private Long prLineSeq; // 기본키

    @ManyToOne
    @JoinColumn(name = "pr_header_seq")
    private PrHeader prHeader; // pr 현황

    @ManyToOne
    @JoinColumn(name = "item_no")
    private Item item; // 아이템

    @Column(name = "rfq_no")
    private Long rfqNo; // RFQ 번호

    @Column(name = "unit_price")
    private Long unitPrice; // 단가

    @Column(name = "pr_quantity")
    private Long prQuantity; // 수량

    @Column(name = "pr_line_price")
    private Long prLinePrice; // 금액(단가 * 수량)

    @Column(name = "note_to_buyer")
    private String noteToBuyer; // 요청 사항

    @Column(name = "need_by_date")
    private LocalDate needByDate; // 요청 납기 일자

    @Column(name = "pr_line")
    private Long prLine; // 라인 순번

    @Column(name = "buyer_no")
    private Long buyerNo; // 바이어 번호
}
