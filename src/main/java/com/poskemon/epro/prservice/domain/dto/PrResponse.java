package com.poskemon.epro.prservice.domain.dto;

import com.poskemon.epro.prservice.domain.entity.Item;
import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PrResponse {
    private Long prHeaderSeq; // 기본키
    private String prNo; // PR번호, 생성년도(YY)+"PP"+000001 (여섯자리 일련번호)
    private String requesterNo; // 신청자
    private String prTitle; // 해당 pr 건명
    private String prLine; // 라인 순번
    private Item item; // 아이템(아이템 명, 카테코리, 사양, uom)
    private Long prQuantity; // 수량
    private Long unitPrice; // 단가
    private Long buyerNo; // 바이어 번호
    private String noteToBuyer; // 요청 사항

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate needByDate; // 요청 납기 일자
}
