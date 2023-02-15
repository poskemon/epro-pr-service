package com.poskemon.epro.prservice.domain.entity;

import com.poskemon.epro.prservice.common.constants.PrStatus;
import java.time.LocalDateTime;
import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 구매 신청 현황(pr_header) 엔티티
 */
@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "pr_header")
@SequenceGenerator(
    name = "pr_header_seq_generator",
    sequenceName = "pr_header_seq", // 매핑할 데이터베이스 시퀀스 이름
    initialValue = 1000, allocationSize = 1)
public class PrHeader {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE,
                    generator = "pr_header_seq_generator")
    @Column(name = "pr_header_seq")
    private Long prHeaderSeq; // 기본키

    @Column(name = "requester_no")
    private Long requesterNo; // 신청자 번호

    @Column(name = "pr_no")
    private String prNo; // 생성년도(YY)+"PP"+000001 (여섯자리 일련번호)

    @Column(name = "pr_title")
    private String prTitle; // 해당 pr 건명

    @Column(name = "pr_creation_date")
    private LocalDateTime prCreationDate; // 생성일자

    @Column(name = "pr_approved_date")
    private LocalDateTime prApprovedDate; // 승인일자

    @Column(name = "pr_status")
    private String prStatus; // pr 진행상태

    @Column(name = "pr_price")
    private Long prPrice; // pr Lines 총 금액
}
