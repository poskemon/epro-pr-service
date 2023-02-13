package com.poskemon.epro.prservice.controller;

import com.poskemon.epro.prservice.domain.dto.PrRequest;
import com.poskemon.epro.prservice.domain.dto.PrResponse;
import com.poskemon.epro.prservice.domain.entity.Item;
import com.poskemon.epro.prservice.domain.entity.PrHeader;
import com.poskemon.epro.prservice.domain.entity.PrLine;
import com.poskemon.epro.prservice.service.ItemService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class PRLineController {

    private final ItemService itemService;

    /**
     * 아이템 목록 조회
     * @return 아이템 리스트
     */
    @GetMapping("/items")
    public List<Item> findAllItems() {
        return itemService.findAllItems();
    }

    @PostMapping("/pr")
    public PrResponse prResgist(List<PrLine> prLines, PrHeader prHeader) {
        return null;
    }
}
