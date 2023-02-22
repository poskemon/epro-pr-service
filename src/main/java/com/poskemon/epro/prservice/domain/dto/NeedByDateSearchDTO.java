package com.poskemon.epro.prservice.domain.dto;

import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class NeedByDateSearchDTO {
    private Long rfqNo;
    private LocalDate needByDate;

    public NeedByDateSearchDTO(NeedByDateSearch needByDateSearch) {
        this.rfqNo = needByDateSearch.getRfqNo();
        this.needByDate = needByDateSearch.getNeedByDate();
    }
}
