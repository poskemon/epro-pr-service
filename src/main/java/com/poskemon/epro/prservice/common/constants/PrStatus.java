package com.poskemon.epro.prservice.common.constants;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum PrStatus {
    ENROLLED("등록완료"),
    APPROVED("승인완료"),
    REQUEST("승인요청");

    private final String prStatus;
}
