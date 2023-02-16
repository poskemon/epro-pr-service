package com.poskemon.epro.prservice.controller;

import com.poskemon.epro.prservice.common.constants.Message;
import com.poskemon.epro.prservice.common.constants.PrStatus;
import com.poskemon.epro.prservice.domain.dto.PrCreateRes;
import com.poskemon.epro.prservice.domain.dto.PrRequest;
import com.poskemon.epro.prservice.domain.dto.PrResponse;
import com.poskemon.epro.prservice.domain.entity.Item;
import com.poskemon.epro.prservice.domain.entity.PrHeader;
import com.poskemon.epro.prservice.service.ItemService;
import java.util.List;

import com.poskemon.epro.prservice.service.PrLineService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class PrLineController {

    private final ItemService itemService;
    private final PrLineService prLineService;

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
     * @return prHeader, prLines
     */
    @PostMapping("/pr")
    public ResponseEntity<PrResponse> prResgist(@RequestBody PrRequest prRequest) {
        try {
            PrResponse prResponse = prLineService.prRegist(prRequest.getPrHeader(), prRequest.getPrLines());
            return ResponseEntity.ok().body(prResponse);
        } catch (Exception e) {
            PrResponse prResponse = PrResponse.builder().message("구매 요청 저장에 실패하였습니다.").build();
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
            prLineService.changeStatus(status, prHeader.getPrNo());
            PrResponse prResponse = PrResponse.builder().message("진행 상태가 변경되었습니다.").build();
            return ResponseEntity.ok().body(prResponse);
        } catch (Exception e) {
            PrResponse prResponse = PrResponse.builder().message("진행 상태가 변경되지 않았습니다.").build();
            return ResponseEntity.badRequest().body(prResponse);
        }
    }
}
