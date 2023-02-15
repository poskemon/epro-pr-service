package com.poskemon.epro.prservice.domain.dto;

import com.poskemon.epro.prservice.common.constants.PrStatus;
import java.util.List;

import com.poskemon.epro.prservice.domain.entity.PrHeader;
import com.poskemon.epro.prservice.domain.entity.PrLine;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PrRequest {
    private PrHeader prHeader;
    private List<PrLine> prLines;
}
