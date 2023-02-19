package com.poskemon.epro.prservice.service.impl;

import com.poskemon.epro.prservice.common.constants.PrStatus;
import com.poskemon.epro.prservice.domain.dto.PrDetailRes;
import com.poskemon.epro.prservice.domain.dto.PrRequest;
import com.poskemon.epro.prservice.domain.dto.UserDTO;
import com.poskemon.epro.prservice.domain.entity.Item;
import com.poskemon.epro.prservice.domain.entity.PrHeader;
import com.poskemon.epro.prservice.domain.entity.PrLine;
import com.poskemon.epro.prservice.repository.ItemRepository;
import com.poskemon.epro.prservice.repository.PrHeaderRepository;
import com.poskemon.epro.prservice.repository.PrLineRepository;
import com.poskemon.epro.prservice.service.PrService;

import com.poskemon.epro.prservice.service.WebClientService;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.Collectors;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class PrServiceImpl implements PrService {

    private final PrLineRepository prLineRepository;
    private final PrHeaderRepository prHeaderRepository;
    private final ItemRepository itemRepository;
    private final WebClientService webClientService;

    @Override
    @Transactional
    public Long prRegist(PrHeader prHeader, List<PrLine> prLines) {
        // 등록 일시(시간 포함)
        LocalDateTime localDateTime = LocalDateTime.now();
        // pr_no : 생성년도(YY)+"PP"+000001 (여섯자리 일련번호)
        String year = localDateTime.format(DateTimeFormatter.ofPattern("yyyy"));
        String uuid = UUID.randomUUID().toString().replace("-", "").replaceAll("[^0-9]", ""); // uuid 에서 '-', 문자 제거
        String prNo = year + "PP" + uuid.substring(uuid.length() - 6, uuid.length());

        // pr header 저장
        prHeader.setPrCreationDate(localDateTime);
        prHeader.setPrNo(prNo);
        prHeader.setPrStatus(PrStatus.ENROLLED.getPrStatus());
        PrHeader savedPrHeader = prHeaderRepository.save(prHeader);

        // pr header 하나에 여러 pr line을 저장
        List<PrLine> resPrLines = new LinkedList<>();
        for (PrLine prLine : prLines) {
            prLine.setPrHeader(savedPrHeader);
            resPrLines.add(prLine);
        }
        prLineRepository.saveAll(resPrLines);

        return savedPrHeader.getPrHeaderSeq();
    }

    @Override
    public Integer changeStatus(String prStatus, String prNo) {
        return prHeaderRepository.changeStatus(prStatus, prNo);
    }

    @Override
    public PrHeader getPrHeaderDetail(Long prHeaderSeq) {
        return prHeaderRepository.findByPrHeaderSeq(prHeaderSeq);
    }

    @Override
    public List<PrDetailRes> getPrDetail(PrHeader prHeader) {
        List<PrLine> prLines = prLineRepository.findAllByPrHeader(prHeader);

        // item 조회
        List<Item> items = itemRepository.findAll();
        for (int i = 0; i < prLines.size(); i++) {
            Long itemNo = prLines.get(i).getItem().getItemNo();
            if (items.get(i).getItemNo().equals(itemNo)) {
                prLines.get(i).setItem(items.get(i));
            }
        }
        List<PrDetailRes> prDetailResList = prLines.stream().map(PrDetailRes::new).collect(Collectors.toList());

        // buyer 조회
        List<Long> buyerNos = prLines.stream().map(prLine -> prLine.getBuyerNo()).collect(Collectors.toList());
        List<UserDTO> users = webClientService.findByBuyerNo(buyerNos);
        if (!Objects.isNull(users)) {
            for (int i = 0; i < prDetailResList.size(); i++) {
                Long buyerNo = prDetailResList.get(i).getBuyerNo();
                if (users.get(i).getUserNo().equals(buyerNo)) {
                    prDetailResList.get(i).setBuyerName(users.get(i).getUserName());
                }
            }
        }

        return prDetailResList;
    }

    @Override
    @Transactional
    public Long modifyPr(PrRequest prRequest) {
        PrHeader prHeader = prHeaderRepository.save(prRequest.getPrHeader());
        // prHeaderSeq에 해당하는 prLine 수정
        List<PrLine> prLines = new LinkedList<>();
        for (PrLine prLine : prRequest.getPrLines()) {
            prLine.setPrHeader(prHeader);
            prLines.add(prLine);
        }
        prLineRepository.saveAll(prLines);
        return prHeader.getPrHeaderSeq();
    }
}
