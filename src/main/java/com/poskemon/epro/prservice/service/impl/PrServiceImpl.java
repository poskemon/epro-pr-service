package com.poskemon.epro.prservice.service.impl;

import com.google.gson.Gson;
import com.poskemon.epro.prservice.common.constants.PrStatus;
import com.poskemon.epro.prservice.domain.dto.NeedByDateSearch;
import com.poskemon.epro.prservice.domain.dto.NeedByDateSearchDTO;
import com.poskemon.epro.prservice.domain.dto.PoInfo;
import com.poskemon.epro.prservice.domain.dto.PrDetailRes;
import com.poskemon.epro.prservice.domain.dto.PrHeaderInfo;
import com.poskemon.epro.prservice.domain.dto.PrRequest;
import com.poskemon.epro.prservice.domain.dto.PrRetrieveReq;
import com.poskemon.epro.prservice.domain.dto.PrRetrieveRes;
import com.poskemon.epro.prservice.domain.dto.PrUpdateDTO;
import com.poskemon.epro.prservice.domain.dto.PurchaseUnitReq;
import com.poskemon.epro.prservice.domain.dto.PurchaseUnitRes;
import com.poskemon.epro.prservice.domain.dto.UserInfoDTO;
import com.poskemon.epro.prservice.domain.entity.Item;
import com.poskemon.epro.prservice.domain.entity.PrHeader;
import com.poskemon.epro.prservice.domain.entity.PrLine;
import com.poskemon.epro.prservice.exception.PrLineException;
import com.poskemon.epro.prservice.repository.ItemRepository;
import com.poskemon.epro.prservice.repository.PrHeaderRepository;
import com.poskemon.epro.prservice.repository.PrLineRepository;
import com.poskemon.epro.prservice.service.PrService;
import com.poskemon.epro.prservice.service.WebClientService;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class PrServiceImpl implements PrService {

    private final PrLineRepository prLineRepository;
    private final PrHeaderRepository prHeaderRepository;
    private final ItemRepository itemRepository;
    private final WebClientService webClientService;

    /**
     * PR 등록
     *
     * @param prHeader 등록할 PrHeader
     * @param prLines  등록할 PrLines
     * @return 등록된 prHeader 기본키 (prHeaderSeq)
     */
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

    /**
     * 상태 변경 (승인요청, 승인완료)
     *
     * @param prStatus 변경하려는 상태
     * @param prNo     변경하려는 prNo
     */
    @Override
    public void changeStatus(String prStatus, String prNo) {
        prHeaderRepository.changeStatus(prStatus, prNo);
    }

    /**
     * PrHeader 상세 조회
     *
     * @param prHeaderSeq 조회하려는 prHeader의 기본키 값
     * @return 조회된 PrHeader
     */
    @Override
    public PrHeader getPrHeaderDetail(Long prHeaderSeq) {
        return prHeaderRepository.findByPrHeaderSeq(prHeaderSeq);
    }

    /**
     * PrLines 상세 조회
     * PrHeader 값으로 PrLines를 조회하고, buyer 이름은 webClient를 통해 user 서비스에서 가져옴
     *
     * @param prHeader 상세 조회하려는 PrHeader
     * @return 조회된 PrLines
     */
    @Override
    public List<PrDetailRes> getPrLinesDetail(PrHeader prHeader) {
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
        if (!prDetailResList.isEmpty()) {
            List<Long> buyerNos = prLines.stream().map(prLine -> prLine.getBuyerNo()).collect(Collectors.toList());
            List<UserInfoDTO> users = webClientService.findUsersByUserNo(buyerNos);
            if (users != null) {
                for (PrDetailRes prDetailRes : prDetailResList) {
                    Long buyerNo = prDetailRes.getBuyerNo();
                    for (UserInfoDTO user : users) {
                        if (user.getUserNo().equals(buyerNo)) {
                            prDetailRes.setBuyerName(user.getUserName());
                            break;
                        }
                    }
                }
            }
        }

        return prDetailResList;
    }

    /**
     * PR 수정
     *
     * @param prRequest 수정 내용 (PrHeader, PrLines)
     * @return prHeaderSeq (수정된 PrHeader의 기본키 값)
     */
    @Override
    @Transactional
    public Long modifyPr(PrRequest prRequest) {
        PrHeader prHeader = prHeaderRepository.save(prRequest.getPrHeader());
        // prHeaderSeq에 해당하는 prLine 삭제
        prLineRepository.deleteAllByPrHeader(prHeader);
        // prLines 등록
        List<PrLine> prLines = new LinkedList<>();
        for (PrLine prLine : prRequest.getPrLines()) {
            prLine.setPrHeader(prHeader);
            prLines.add(prLine);
        }
        prLineRepository.saveAll(prLines);
        return prHeader.getPrHeaderSeq();
    }

    /**
     * PR 삭제
     * PrHeader와 PrLines를 모두 삭제한다.
     *
     * @param prHeaderSeq 삭제하려는 PrHeader의 기본키 값
     */
    @Override
    @Transactional
    public void deletePr(Long prHeaderSeq) {
        PrHeader prHeader = prHeaderRepository.findByPrHeaderSeq(prHeaderSeq);
        prLineRepository.deleteAllByPrHeader(prHeader);
        prHeaderRepository.deleteById(prHeaderSeq);
    }

    /**
     * PR 목록 조회
     * 구매단위편성 화면에서 사용
     *
     * @param purchaseUnitReq 구매단위편성 조회 조건
     * @return 조회된 PR 목록
     */
    @Override
    public List<PurchaseUnitRes> getAllPrWithParams(PurchaseUnitReq purchaseUnitReq) {
        List<PrLine> prLines = prLineRepository.findAllPrWithParams(purchaseUnitReq);
        List<PurchaseUnitRes> purchaseUnitResList = prLines.stream()
                                                           .map(PurchaseUnitRes::new)
                                                           .collect(Collectors.toList());
        // 조회결과가 있을 경우 아래의 코드 실행
        if (!purchaseUnitResList.isEmpty()) {
            // buyerNo, requesterNo 리스트
            List<Long> buyerNos = purchaseUnitResList.stream()
                                                     .map(purchaseUnitRes -> purchaseUnitRes.getBuyerNo())
                                                     .collect(Collectors.toList());
            List<Long> requesterNos = purchaseUnitResList.stream()
                                                         .map(purchaseUnitRes -> purchaseUnitRes.getRequesterNo())
                                                         .collect(Collectors.toList());

            // userService 에서 buyerName, requesterName 조회
            List<UserInfoDTO> buyers = webClientService.findUsersByUserNo(buyerNos);
            List<UserInfoDTO> requesters = webClientService.findUsersByUserNo(requesterNos);

            if (buyers != null) {
                for (PurchaseUnitRes purchaseUnitRes : purchaseUnitResList) {
                    Long buyerNo = purchaseUnitRes.getBuyerNo();
                    for (UserInfoDTO buyer : buyers) {
                        if (buyer.getUserNo().equals(buyerNo)) {
                            purchaseUnitRes.setBuyerName(buyer.getUserName());
                            break;
                        }
                    }
                }
            }

            if (requesters != null) {
                for (PurchaseUnitRes purchaseUnitRes : purchaseUnitResList) {
                    Long requesterNo = purchaseUnitRes.getRequesterNo();
                    for (UserInfoDTO requester : requesters) {
                        if (requester.getUserNo().equals(requesterNo)) {
                            purchaseUnitRes.setRequesterName(requester.getUserName());
                            break;
                        }
                    }
                }
            }

            // set elapsedDate (현재날짜 - 승인일)
            LocalDateTime today = LocalDateTime.now();
            for (int i = 0; i < purchaseUnitResList.size(); i++) {
                LocalDateTime approvedDate = prLines.get(i).getPrHeader().getPrApprovedDate();
                if (approvedDate != null) {
                    LocalDate todayDate = today.toLocalDate();
                    LocalDate approvedLocalDate = approvedDate.toLocalDate();
                    long elapsedDays = ChronoUnit.DAYS.between(approvedLocalDate, todayDate);
                    purchaseUnitResList.get(i).setElapsedDate(elapsedDays);
                }
            }
        }

        return purchaseUnitResList;
    }

    /**
     * PR 목록 조회
     * 구매신청현황 화면에서 사용
     *
     * @param prRetrieveReq 구매신청현황 조회 조건
     * @return 조회된 PR목록
     */
    @Override
    public List<PrRetrieveRes> getAllPr(PrRetrieveReq prRetrieveReq) {
        List<PrLine> prLines = prLineRepository.findAllPr(prRetrieveReq);
        List<PrRetrieveRes> prRetrieveResList = prLines.stream()
                                                       .map(PrRetrieveRes::new)
                                                       .collect(Collectors.toList());

        // 조회결과가 있을 경우 아래의 코드 실행
        if (!prRetrieveResList.isEmpty()) {
            // buyerNo, requesterNo, rfqNo 리스트
            List<Long> buyerNos = prRetrieveResList.stream()
                                                   .map(prRetrieveRes -> prRetrieveRes.getBuyerNo())
                                                   .collect(Collectors.toList());
            List<Long> requesterNos = prRetrieveResList.stream()
                                                       .map(prRetrieveRes -> prRetrieveRes.getRequesterNo())
                                                       .collect(Collectors.toList());
            List<Long> rfqNos = prRetrieveResList.stream()
                                                 .map(prRetrieveRes -> prRetrieveRes.getRfqNo())
                                                 .collect(Collectors.toList());
            rfqNos.removeIf(Objects::isNull); // rfqNo null 값 제거

            // userService 에서 buyerName, requesterName 조회
            List<UserInfoDTO> buyers = webClientService.findUsersByUserNo(buyerNos);
            List<UserInfoDTO> requesters = webClientService.findUsersByUserNo(requesterNos);
            // poService 에서 poNo, price 조회
            List<PoInfo> poInfos = webClientService.getPoInfoByRfqNo(rfqNos);

            if (buyers != null) {
                for (PrRetrieveRes prRetrieveRes : prRetrieveResList) {
                    Long buyerNo = prRetrieveRes.getBuyerNo();
                    for (UserInfoDTO buyer : buyers) {
                        if (buyer.getUserNo().equals(buyerNo)) {
                            prRetrieveRes.setBuyerName(buyer.getUserName());
                            break;
                        }
                    }
                }
            }

            if (requesters != null) {
                for (PrRetrieveRes prRetrieveRes : prRetrieveResList) {
                    Long requesterNo = prRetrieveRes.getRequesterNo();
                    for (UserInfoDTO requester : requesters) {
                        if (requester.getUserNo().equals(requesterNo)) {
                            prRetrieveRes.setRequesterName(requester.getUserName());
                            break;
                        }
                    }
                }
            }

            if (poInfos != null) {
                for (PrRetrieveRes prRetrieveRes : prRetrieveResList) {
                    Long rfqNo = prRetrieveRes.getRfqNo();
                    for (PoInfo poInfo : poInfos) {
                        if (poInfo.getRfqNo().equals(rfqNo)) {
                            prRetrieveRes.setPoNo(poInfo.getPoNo());
                            prRetrieveRes.setPoPrice(poInfo.getPoPrice());
                            break;
                        }
                    }
                }
            }
        }
        return prRetrieveResList;
    }

    /**
     * RfqNo 생성
     * kafka를 통해 데이터 동기화
     *
     * @param message kafka에게 받은 문자열 데이터
     * @throws IOException
     */
    @Transactional
    @KafkaListener(topics = "pr-update", groupId = "pr-service")
    public void setRfqNo(String message) throws IOException {
        System.out.println("receive message : " + message);

        PrUpdateDTO prUpdateDTO = new Gson().fromJson(message, PrUpdateDTO.class);

        // prLine의 rfqNo등록
        List<PrLine> prLines = prLineRepository.findAllByPrNoAndPrLine(prUpdateDTO);
        for (int i = 0; i < prLines.size(); i++) {
            prLines.get(i).setRfqNo(prUpdateDTO.getRfqNo());
        }

        // TODO - 관리자가 승인할 때 승인완료 상태로 변경
        // prHeader의 진행상태 승인완료로 변경
        // prHeader의 승인완료일 현재시각으로 등록
        // PrHeader prHeader = prHeaderRepository.findByPrNo(prUpdateDTO.getPrNo());
        // LocalDateTime localDateTime = LocalDateTime.now(); // 현재시각
        // prHeader.setPrStatus(PrStatus.APPROVED.getPrStatus());
        // prHeader.setPrApprovedDate(localDateTime);

        prLineRepository.saveAll(prLines);
        // prHeaderRepository.save(prHeader);
    }

    /**
     * 최대 납기일 조회
     * rfqNo에 해당하는 PrLine 목록 중 최대 납기일 조회
     *
     * @param rfqNos rfqNo 리스트
     * @return rfqNo, NeedByDate 리스트
     */
    @Override
    public List<NeedByDateSearchDTO> getNeedByDateByRfqNo(List<Long> rfqNos) {
        List<NeedByDateSearch> needBydates = prLineRepository.findAllByRfqNos(rfqNos);
        List<NeedByDateSearchDTO> needByDateSearchDTOS = needBydates.stream()
                                                                    .map(NeedByDateSearchDTO::new)
                                                                    .collect(Collectors.toList());
        return needByDateSearchDTOS;
    }

    @Transactional(readOnly = true)
    @Override
    public List<Long> retrieveItemInfoByRfqNo(List<Long> rfqNos) {
        return prLineRepository.findItemNoByRfqNo(rfqNos);
    }

    @Transactional(readOnly = true)
    @Override
    public List<PrHeaderInfo> retrievePrInfoByRfqNo(Long rfqNo) {
        if (prLineRepository.findPrLinesByRfqNo(rfqNo).isEmpty()) {
            throw new PrLineException();
        } else {
            PrLine prLine = prLineRepository.findPrLinesByRfqNo(rfqNo).get(0);
            return prLineRepository.findPrInfo(prLine.getPrHeader().getPrHeaderSeq());
        }
    }
}
