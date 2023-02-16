package com.poskemon.epro.prservice.service;

import com.poskemon.epro.prservice.domain.dto.PrResponse;
import com.poskemon.epro.prservice.domain.entity.PrHeader;
import com.poskemon.epro.prservice.domain.entity.PrLine;
import java.util.List;

public interface PrLineService {
    public PrResponse prRegist(PrHeader prHeader, List<PrLine> prLines);
    public Integer changeStatus(String prStatus, String prNo);
}
