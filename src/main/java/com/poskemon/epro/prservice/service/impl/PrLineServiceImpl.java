package com.poskemon.epro.prservice.service.impl;

import com.poskemon.epro.prservice.common.constants.Message;
import com.poskemon.epro.prservice.common.constants.PrStatus;
import com.poskemon.epro.prservice.domain.dto.PrCreateRes;
import com.poskemon.epro.prservice.domain.dto.PrResponse;
import com.poskemon.epro.prservice.domain.dto.UserDTO;
import com.poskemon.epro.prservice.domain.entity.PrHeader;
import com.poskemon.epro.prservice.domain.entity.PrLine;
import com.poskemon.epro.prservice.repository.PrHeaderRepository;
import com.poskemon.epro.prservice.repository.PrLineRepository;
import com.poskemon.epro.prservice.service.PrLineService;

import com.poskemon.epro.prservice.service.WebClientService;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class PrLineServiceImpl implements PrLineService {

    private final PrLineRepository prLineRepository;
    private final PrHeaderRepository prHeaderRepository;
    private final WebClientService webClientService;

    @Override
    @Transactional
    public PrResponse prRegist(PrHeader prHeader, List<PrLine> prLines) {
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
        List<PrLine> savedPrLines = prLineRepository.saveAll(resPrLines);

        if (savedPrLines.isEmpty()) {
            return PrResponse.builder().message(Message.SAVE_DATA_FAIL.getMessage()).build();
        }

        // 바이어 이름 요청
        List<PrCreateRes> prCreateResList = prCreateRes(savedPrLines);

        PrResponse prResponse = new PrResponse<>();
        prResponse.setPrHeader(savedPrHeader);
        prResponse.setPrCreateRes(prCreateResList);
        return prResponse;
    }

    public List<PrCreateRes> prCreateRes(List<PrLine> savedPrLines) {
        List<Long> buyerNos = new LinkedList<>();
        List<Long> prLineSeqs = new LinkedList<>();
        for(PrLine prLine : savedPrLines) {
            buyerNos.add(prLine.getBuyerNo());
            prLineSeqs.add(prLine.getPrLineSeq());
        }

        List<UserDTO> buyers = webClientService.findByBuyerNo(buyerNos); // user 서비스에 요청
        List<PrCreateRes> returnPrCreate = new LinkedList<>();
        for (int i = 0; i < savedPrLines.size(); i++) {
            String buyer = "";
            if (buyers != null) {
                for (UserDTO user : buyers) {
                    if (user.getUserNo().equals(savedPrLines.get(i).getBuyerNo()))
                        buyer = user.getUserName(); // 바이어 번호에 맞는 이름 등록
                }
            }

            returnPrCreate.add(new PrCreateRes(
                savedPrLines.get(i).getPrLineSeq(),
                savedPrLines.get(i).getItem(),
                savedPrLines.get(i).getRfqNo(),
                savedPrLines.get(i).getUnitPrice(),
                savedPrLines.get(i).getPrQuantity(),
                savedPrLines.get(i).getPrLinePrice(),
                savedPrLines.get(i).getNoteToBuyer(),
                savedPrLines.get(i).getNeedByDate(),
                savedPrLines.get(i).getPrLine(),
                savedPrLines.get(i).getBuyerNo(),
                buyer
            ));
        }
        return returnPrCreate;
    }

    @Override
    public Integer changeStatus(String prStatus, String prNo) {
        return prHeaderRepository.changeStatus(prStatus, prNo);
    }
}
