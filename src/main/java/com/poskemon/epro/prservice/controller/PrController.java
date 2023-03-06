package com.poskemon.epro.prservice.controller;

import com.poskemon.epro.prservice.common.constants.PrStatus;
import com.poskemon.epro.prservice.common.constants.UserRole;
import com.poskemon.epro.prservice.domain.dto.NeedByDateSearchDTO;
import com.poskemon.epro.prservice.domain.dto.PrApprovalParam;
import com.poskemon.epro.prservice.domain.dto.PrDetailRes;
import com.poskemon.epro.prservice.domain.dto.PrHeaderDetailRes;
import com.poskemon.epro.prservice.domain.dto.PrHeaderInfo;
import com.poskemon.epro.prservice.domain.dto.PrRequest;
import com.poskemon.epro.prservice.domain.dto.PrResponse;
import com.poskemon.epro.prservice.domain.dto.PrRetrieveReq;
import com.poskemon.epro.prservice.domain.dto.PrRetrieveRes;
import com.poskemon.epro.prservice.domain.dto.PurchaseUnitReq;
import com.poskemon.epro.prservice.domain.dto.PurchaseUnitRes;
import com.poskemon.epro.prservice.domain.dto.UserInfoDTO;
import com.poskemon.epro.prservice.domain.entity.Item;
import com.poskemon.epro.prservice.domain.entity.PrHeader;
import com.poskemon.epro.prservice.service.ItemService;
import com.poskemon.epro.prservice.service.PrService;
import com.poskemon.epro.prservice.service.WebClientService;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
public class PrController {

    private final ItemService itemService;
    private final PrService prService;
    private final WebClientService webClientService;

    /**
     * 아이템 전체 조회
     * 구매등록 시 아이템 선택을 위한 API
     *
     * @return 아이템 리스트
     */
    @GetMapping("/items")
    public List<Item> findAllItems() {
        return itemService.findAllItems();
    }

    /**
     * 아이템 명으로 전체 조회
     *
     * @param itemDescription 아이템 명
     * @return 아이템 리스트
     */
    @GetMapping("/items/search")
    public List<Item> findItemsByDesc(@RequestParam(required = false) String itemDescription) {
        return itemService.findItemsByDesc(itemDescription);
    }

    /**
     * 바이어 전체 조회
     * 구매 등록 시 바이어 선택을 위한 API
     * webClient를 통해 user 서비스에서 가져옴
     *
     * @return 바이어 리스트
     */
    @GetMapping("/buyers")
    public List<UserInfoDTO> findAllBuyers() {
        List<UserInfoDTO> users = webClientService.findUsersByRole(UserRole.BUYER.getCode());
        return users;
    }

    /**
     * pr 등록
     *
     * @param prRequest 등록 내용 (PrHeader, PrLines)
     * @return ResponseEntity (등록된 PrHeader, PrLines)
     */
    @PostMapping("/pr")
    public ResponseEntity<?> prResgist(@RequestBody PrRequest prRequest) {
        try {
            Long prHeaderSeq = prService.prRegist(prRequest.getPrHeader(), prRequest.getPrLines());
            return ResponseEntity.ok().body(prHeaderSeq);
        } catch (Exception e) {
            PrResponse prResponse = PrResponse.builder().message(e.getMessage()).build();
            return ResponseEntity.badRequest().body(prResponse);
        }
    }

    /**
     * 구매신청 진행상태 변경 (승인요청, 승인완료)
     *
     * @return ResponseEntity (변경 결과)
     */
    @PutMapping("/pr/status")
    public ResponseEntity<?> changeStatus(@RequestBody PrApprovalParam prApprovalParam) {
        try {
            String prStatus = PrStatus.valueOfName(prApprovalParam.getPrStatus()).getPrStatus();
            PrHeader prHeader = prService.changeStatus(prStatus, prApprovalParam.getPrHeaderSeq());
            return ResponseEntity.ok().body(prHeader);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    /**
     * 구매신청 상세조회
     *
     * @param prHeaderSeq 조회하려는 PrHeader의 기본키 값
     * @return ResponseEntity (조회된 PR)
     */
    @GetMapping("/pr/{prHeaderSeq}")
    public ResponseEntity<?> getPrDetails(@PathVariable Long prHeaderSeq) {
        try {
            PrHeaderDetailRes prHeaderDetailRes = prService.getPrHeaderDetail(prHeaderSeq);

            PrHeader prHeader = prService.getPrHeader(prHeaderSeq);
            List<PrDetailRes> prDetailResList = prService.getPrLinesDetail(prHeader);

            PrResponse prResponse = PrResponse.builder().prHeader(prHeaderDetailRes).prLines(prDetailResList).build();
            return ResponseEntity.ok().body(prResponse);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    /**
     * 구매신청 수정
     * 등록완료 상태에서만 수정 가능함.
     *
     * @param prRequest 수정 내용 (PrHeader, PrLines)
     * @return ResponseEntity (prHeaderSeq)
     */
    @PutMapping("/pr")
    public ResponseEntity<?> modifyPr(@RequestBody PrRequest prRequest) {
        try {
            Long modifiedPrHeaderSeq = prService.modifyPr(prRequest);
            return ResponseEntity.ok().body(modifiedPrHeaderSeq);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    /**
     * 구매신청 삭제
     * 승인요청 상태에서 구매신청 내용을 변경하고 싶은 경우 삭제 후 다시 등록해야 함.
     *
     * @param prHeaderSeq 조회하려는 PrHeader의 기본키 값
     * @return ResponseEntity (삭제 결과 메세지)
     */
    @DeleteMapping("/pr/{prHeaderSeq}")
    public ResponseEntity<String> deletePr(@PathVariable Long prHeaderSeq) {
        try {
            // 승인완료 상태인지 확인
            PrHeader prHeader = prService.getPrHeader(prHeaderSeq);
            if (PrStatus.APPROVED.getPrStatus().equals(prHeader.getPrStatus())) {
                throw new RuntimeException("승인완료된 구매신청 건은 삭제할 수 없습니다.");
            }
            prService.deletePr(prHeaderSeq);
            return ResponseEntity.ok().body("삭제 완료되었습니다.");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    /**
     * PR 목록 조회
     * 구매단위편성 화면에서 사용
     *
     * @param purchaseUnitReq 구매단위편성 조회 조건
     * @return 조회된 PR 목록
     */
    @GetMapping("/pr-line")
    public ResponseEntity<?> getPrLinesForPrUnit(PurchaseUnitReq purchaseUnitReq) {
        try {
            purchaseUnitReq.setPrStatus(PrStatus.APPROVED.getPrStatus()); // 승인 완료된 것만 조회
            if (purchaseUnitReq.getRequesterNo() == null) {
                purchaseUnitReq.setRequesterNo(-1L);
            }
            if (purchaseUnitReq.getBuyerNo() == null) {
                purchaseUnitReq.setBuyerNo(-1L);
            }
            if (purchaseUnitReq.getRfqNo() == null) {
                purchaseUnitReq.setRfqNo(-1L);
            }
            List<PurchaseUnitRes> purchaseUnitResList = prService.getAllPrWithParams(purchaseUnitReq);
            return ResponseEntity.ok().body(purchaseUnitResList);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    /**
     * PR 목록 조회
     * 구매신청현황 화면에서 사용
     *
     * @param prRetrieveReq 구매신청현황 조회 조건
     * @return 조회된 PR 목록
     */
    @GetMapping("/pr/search")
    public ResponseEntity<?> getPrLines(PrRetrieveReq prRetrieveReq) {
        try {
            if (prRetrieveReq.getRequesterNo() == null) {
                prRetrieveReq.setRequesterNo(-1L);
            }
            if (prRetrieveReq.getBuyerNo() == null) {
                prRetrieveReq.setBuyerNo(-1L);
            }
            if (prRetrieveReq.getItemNo() == null) {
                prRetrieveReq.setItemNo(-1L);
            }
            if (prRetrieveReq.getPrStatus() == null || prRetrieveReq.getPrStatus().isEmpty()) {
                List<String> prStatusList = Arrays.asList(PrStatus.APPROVED.getPrStatus(), PrStatus.ENROLLED.getPrStatus(), PrStatus.REQUEST.getPrStatus());
                prRetrieveReq.setPrStatus(prStatusList);
            }
            if (prRetrieveReq.getPrCreationDate() != null) {
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

                LocalDateTime searchStartTime = LocalDate.parse(prRetrieveReq.getPrCreationDate(), formatter).atStartOfDay();
                prRetrieveReq.setPrCreationDateStart(searchStartTime);

                LocalDateTime searchEndTime = searchStartTime.plusDays(1); // 1일 후
                prRetrieveReq.setPrCreationDateEnd(searchEndTime);
            }

            List<PrRetrieveRes> prRetrieveResList = prService.getAllPr(prRetrieveReq);
            return ResponseEntity.ok().body(prRetrieveResList);
        } catch (Exception e) {
            log.error(e.getMessage());
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    /**
     * 최대 납기일 조회
     * rfqNo에 해당하는 PrLine 목록 중 최대 납기일 조회
     *
     * @param rfqNos rfqNo 리스트
     * @return rfqNo, NeedByDate 리스트
     */
    @GetMapping("/pr-line/need-by-date/{rfqNos}")
    public List<NeedByDateSearchDTO> getNeedByDateByRfqNo(@PathVariable List<Long> rfqNos) {
        return prService.getNeedByDateByRfqNo(rfqNos);
    }

    /**
     * RFQ 번호 리스트로 ItemNo 모두 조회 API
     * Rfq 리스트로 Item 번호 모두 조회
     *
     * @param rfqNos 조회할 rfq 번호
     * @return 아이템 번호를 리스트로 반환
     */
    @PostMapping("/item-info")
    public List<Long> retrieveItemInfoByRfqNo(@RequestBody List<Long> rfqNos) {
        return prService.retrieveItemInfoByRfqNo(rfqNos);
    }

    /**
     * RfqNo 로 해당하는 PR 정보를 모두 조회
     * 낙찰된 RFQ 정보를 조회할 때 사용
     *
     * @param rfqNo 조회할 rfq 번호
     * @return rfq 번호로 해당하는 PR 모두 조회
     */
    @GetMapping("/pr-line/{rfqNo}")
    public List<PrHeaderInfo> retrievePrInfoByRfqNo(@PathVariable Long rfqNo) {
        return prService.retrievePrInfoByRfqNo(rfqNo);
    }

    @GetMapping("/pr")
    public List<PrHeader> findPrHeader() {
        return prService.findPrHeader();
    }

}
