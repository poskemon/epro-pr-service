package com.poskemon.epro.prservice.service.impl;

import com.poskemon.epro.prservice.domain.dto.PrResponse;
import com.poskemon.epro.prservice.domain.entity.PrHeader;
import com.poskemon.epro.prservice.domain.entity.PrLine;
import com.poskemon.epro.prservice.repository.PrHeaderRepository;
import com.poskemon.epro.prservice.repository.PrLineRepository;
import com.poskemon.epro.prservice.service.PrLineService;

import java.time.LocalDate;
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
    public PrResponse prRegist(PrHeader prHeader, List<PrLine> prLines) {
        // 1. pr header 저장
        LocalDate currentDate = LocalDate.now();
        prHeader.setPrCreationDate(currentDate.atStartOfDay());
        PrHeader savedPrHeader = prHeaderRepository.save(prHeader);
        // 2. pr header 하나에 여러 pr line을 저장
        List<PrLine> newPrLines = new LinkedList<>();
        for (PrLine prLine : prLines) {
            prLine.setPrHeader(savedPrHeader);
            newPrLines.add(prLine);
        }
        List<PrLine> savedPrLines = prLineRepository.saveAll(newPrLines);

        // 3. prResponse 에 담기
        PrResponse prResponse = PrResponse.builder().prHeader(savedPrHeader).prLineList(savedPrLines).build();
        return prResponse;
    }
}
