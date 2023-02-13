package com.poskemon.epro.prservice.service.impl;

import com.poskemon.epro.prservice.domain.entity.PrHeader;
import com.poskemon.epro.prservice.domain.entity.PrLine;
import com.poskemon.epro.prservice.repository.PrHeaderRepository;
import com.poskemon.epro.prservice.repository.PrLineRepository;
import com.poskemon.epro.prservice.service.PrLineService;
import java.util.LinkedList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class PrLineServiceImpl implements PrLineService {

    private final PrLineRepository prLineRepository;
    private final PrHeaderRepository prHeaderRepository;

    @Override
    @Transactional
    public PrLine prRegist(PrHeader prHeader, List<PrLine> prLines) {
        // 1. pr header 저장
        PrHeader savedPrHeader = prHeaderRepository.save(prHeader);
        // 2. pr header 하나에 여러 pr line을 저장
        List<PrLine> newPrLines = new LinkedList<>();
        for (PrLine prLine : prLines) {
            newPrLines.add(prLine.setPrHeader(savedPrHeader));
        }
        PrLine savedPrLine = prLineRepository.save(prLine);

        return null;
    }
}
