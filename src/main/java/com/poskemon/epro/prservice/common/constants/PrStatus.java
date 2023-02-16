package com.poskemon.epro.prservice.common.constants;

import java.util.Arrays;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum PrStatus {
    ENROLLED("등록완료"),
    APPROVED("승인완료"),
    REQUEST("승인요청");

    private final String prStatus;

    public static PrStatus valueOfName(String prStatus) {
        return Arrays.stream(values())
                     .filter(value -> value.prStatus.equals(prStatus))
                     .findAny()
                     .orElseThrow(()-> new RuntimeException("Invalid PR Status"));
    }
}
