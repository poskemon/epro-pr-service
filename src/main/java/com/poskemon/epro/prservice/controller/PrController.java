package com.poskemon.epro.prservice.controller;

import com.poskemon.epro.prservice.common.constants.Message;
import com.poskemon.epro.prservice.common.constants.PrStatus;
import com.poskemon.epro.prservice.common.constants.UserRole;
import com.poskemon.epro.prservice.domain.dto.*;
import com.poskemon.epro.prservice.domain.entity.Item;
import com.poskemon.epro.prservice.domain.entity.PrHeader;
import com.poskemon.epro.prservice.service.ItemService;

import java.util.List;

import com.poskemon.epro.prservice.service.PrService;
import com.poskemon.epro.prservice.service.WebClientService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
    public PrResponse findAllItems() {
        List<Item> foundItems = itemService.findAllItems();
        if (foundItems.isEmpty()) {
            return PrResponse.builder().message(Message.NOT_FOUND_ITEMS.getMessage()).build();
        }
        return PrResponse.builder().itmeList(foundItems).build();
    }

    /**
     * 바이어 전체 조회
     * 구매 등록 시 바이어 선택을 위한 API
     * webClient를 통해 user 서비스에서 가져옴
     *
     * @return 바이어 리스트
     */
    @GetMapping("/buyers")
    public List<UserDTO> findAllBuyers() {
        List<UserDTO> users = webClientService.findUsersByRole(UserRole.BUYER.getCode());
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
     * @return ResponseEntity (변경 결과 메세지)
     */
    @PutMapping("/pr/status")
    public ResponseEntity<PrResponse> changeStatus(@RequestBody PrHeader prHeader) {
        try {
            String status = PrStatus.valueOfName(prHeader.getPrStatus()).getPrStatus();
            prService.changeStatus(status, prHeader.getPrNo());
            PrResponse prResponse = PrResponse.builder().message("진행 상태가 변경되었습니다.").build();
            return ResponseEntity.ok().body(prResponse);
        } catch (Exception e) {
            PrResponse prResponse = PrResponse.builder().message(e.getMessage()).build();
            return ResponseEntity.badRequest().body(prResponse);
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
            PrHeader prHedaer = prService.getPrHeaderDetail(prHeaderSeq);
            List<PrDetailRes> prDetailResList = prService.getPrLinesDetail(prHedaer);
            PrResponse prResponse = PrResponse.builder().prHeader(prHedaer).prLines(prDetailResList).build();
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
            // 승인요청, 승인완료 상태인지 확인
            Long prHeaderSeq = prRequest.getPrHeader().getPrHeaderSeq();
            PrHeader prHeader = prService.getPrHeaderDetail(prHeaderSeq);
            if (!PrStatus.ENROLLED.getPrStatus().equals(prHeader.getPrStatus())) {
                throw new RuntimeException("현재 구매신청 건은 변경할 수 없습니다.");
            }

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
            PrHeader prHeader = prService.getPrHeaderDetail(prHeaderSeq);
            if (PrStatus.APPROVED.getPrStatus().equals(prHeader.getPrStatus())) {
                throw new RuntimeException("승인완료된 구매신청 건은 삭제할 수 없습니다.");
            }
            prService.deletePr(prHeaderSeq);
            return ResponseEntity.ok().body("삭제 완료되었습니다.");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/pr-line")
    public ResponseEntity<?> getPrLinesForPrUnit(PurchaseUnitReq purchaseUnitReq) {
        try {
            if(purchaseUnitReq.getExcept() == null) {
                purchaseUnitReq.setExcept(PrStatus.ENROLLED.getPrStatus());
            }
            if(purchaseUnitReq.getRequesterNo() == null) {
                purchaseUnitReq.setRequesterNo(-1L);
            }
            if(purchaseUnitReq.getBuyerNo() == null) {
                purchaseUnitReq.setBuyerNo(-1L);
            }
            if(purchaseUnitReq.getRfqNo() == null) {
                purchaseUnitReq.setRfqNo(-1L);
            }
            List<PurchaseUnitRes> purchaseUnitResList = prService.getAllPrWithParams(purchaseUnitReq);
            return ResponseEntity.ok().body(purchaseUnitResList);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/pr-line/need-by-date/{rfqNos}")
    public List<RfqDTO> getNeedByDateByRfqNo(@PathVariable List<Long> rfqNos) {
        return prService.getNeedByDateByRfqNo(rfqNos);
    }

}
