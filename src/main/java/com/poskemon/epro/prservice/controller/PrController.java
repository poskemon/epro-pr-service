package com.poskemon.epro.prservice.controller;

import com.poskemon.epro.prservice.common.constants.Message;
import com.poskemon.epro.prservice.common.constants.PrStatus;
import com.poskemon.epro.prservice.domain.dto.PrDetailRes;
import com.poskemon.epro.prservice.domain.dto.PrRequest;
import com.poskemon.epro.prservice.domain.dto.PrResponse;
import com.poskemon.epro.prservice.domain.entity.Item;
import com.poskemon.epro.prservice.domain.entity.PrHeader;
import com.poskemon.epro.prservice.service.ItemService;

import java.util.List;

import com.poskemon.epro.prservice.service.PrService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class PrController {

    private final ItemService itemService;
    private final PrService prService;

    /**
     * 아이템 목록 조회
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
     * pr 등록
     *
     * @param prRequest 등록 내용
     * @return
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
     * 구매신청 진행상태 변경
     *
     * @return
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
     * @param prHeaderSeq
     * @return
     */
    @GetMapping("/pr/{prHeaderSeq}")
    public ResponseEntity<PrResponse> getPrDetails(@PathVariable Long prHeaderSeq) {
        try {
            PrHeader prHedaer = prService.getPrHeaderDetail(prHeaderSeq);
            List<PrDetailRes> prDetailResList = prService.getPrDetail(prHedaer);
            PrResponse prResponse = PrResponse.builder().prHeader(prHedaer).prLines(prDetailResList).build();
            return ResponseEntity.ok().body(prResponse);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(PrResponse.builder().message(e.getMessage()).build());
        }
    }

    /**
     * 구매신청 수정
     *
     * @param prRequest
     * @return
     */
    @PutMapping("/pr")
    public ResponseEntity<?> modifyPr(@RequestBody PrRequest prRequest) {
        // 승인요청, 승인완료 상태인지 확인
        Long prHeaderSeq = prRequest.getPrHeader().getPrHeaderSeq();
        PrHeader prHeader = prService.getPrHeaderDetail(prHeaderSeq);

        // TODO - 승인요청 상태에서도 변경 불가능한거 맞는지 확인
        if(!PrStatus.ENROLLED.getPrStatus().equals(prHeader.getPrStatus())) {
            return ResponseEntity.badRequest().body("현재 구매신청 건은 변경할 수 없습니다.");
        }

        Long modifiedPrHeaderSeq = prService.modifyPr(prRequest);
        return ResponseEntity.ok().body(modifiedPrHeaderSeq);
    }
}
