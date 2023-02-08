package com.poskemon.epro.prservice.controller;

import com.poskemon.epro.prservice.domain.dto.PrLineDTO;
import com.poskemon.epro.prservice.domain.dto.PurchaseUnitDTO;
import java.util.LinkedList;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


/**
 * The type Pr controller.
 */
@RestController
@Slf4j
public class PRController {

    /**
     * Pr line 조회
     * PurchaseUnitDTO condition     react에서 params로 전송
     */
    @GetMapping("/pr-line")
    public List<PrLineDTO> prLine(PurchaseUnitDTO condition) {
        log.info("test");
        log.info(condition.toString());
        List<PrLineDTO> list = new LinkedList<>();
        for (int i = 0; i < 22; i++) {
            list.add(new PrLineDTO("등록상태",
                null,
                "3일",
                "QBurnt Chaff_B-Flux",
                "22PP000001",
                1,
               "Q2065363",
                "Thermal lnsulation 1400",
                "1000원",
                "box",
                 10,
                "황동현",
                "장건우" + i,
                "빨리해줘" + i));
        }

        return list;
    }
}
