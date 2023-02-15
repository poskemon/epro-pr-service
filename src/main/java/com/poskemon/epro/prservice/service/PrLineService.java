package com.poskemon.epro.prservice.service;

import com.poskemon.epro.prservice.domain.entity.PrHeader;
import com.poskemon.epro.prservice.domain.entity.PrLine;
import java.util.List;

public interface PrLineService {
    public List<PrLine> prRegist(PrHeader prHeader, List<PrLine> prLines);
    public Integer changeStatus(String prStatus, String prNo);
}
