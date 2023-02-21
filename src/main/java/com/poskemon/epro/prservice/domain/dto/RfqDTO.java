package com.poskemon.epro.prservice.domain.dto;

import com.poskemon.epro.prservice.domain.entity.PrLine;
import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RfqDTO {
    private Long rfqNo;
    private LocalDate needByDate;

    public RfqDTO(PrLine prLine) {
    this.rfqNo = prLine.getRfqNo();
    this.needByDate = prLine.getNeedByDate();
    }
}
