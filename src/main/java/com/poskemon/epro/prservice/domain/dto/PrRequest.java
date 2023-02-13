package com.poskemon.epro.prservice.domain.dto;

import java.time.LocalDate;
import java.util.List;

import com.poskemon.epro.prservice.domain.entity.PrHeader;
import com.poskemon.epro.prservice.domain.entity.PrLine;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PrRequest {
//    private String requesterNo; // 신청자
//    private String prTitle; // 해당 pr 건명
//    private String prLine; // 라인 순번
//    private String itemNo; // 아이템 번호
//    private Long prQuantity; // 수량
//    private Long unitPrice; // 단가
//    private Long buyerNo; // 바이어 번호
//    private String noteToBuyer; // 요청 사항
//
//    @DateTimeFormat(pattern = "yyyy-MM-dd")
//    private LocalDate needByDate; // 요청 납기 일자
    private PrHeader prHeader;
    private List<PrLine> prLines;
}
