package com.poskemon.epro.prservice.service;

import com.poskemon.epro.prservice.domain.entity.PrHeader;
import com.poskemon.epro.prservice.domain.entity.PrLine;
import java.util.List;

public interface PrLineService {
    public PrLine prRegist(PrHeader prHeader, List<PrLine> prLines);
}
