package com.poskemon.epro.prservice.common.constants;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum Message {
    NOT_FOUND_STATUS("일치하는 상태가 없습니다."),
    NOT_FOUND_ITEMS("등록된 아이템이 없습니다."),

    SAVE_DATA_FAIL("데이터 등록에 실패하였습니다.");

    private String message;
}
