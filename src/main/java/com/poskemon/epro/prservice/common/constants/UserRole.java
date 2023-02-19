package com.poskemon.epro.prservice.common.constants;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum UserRole {
    REQUESTER("사용부서", 1),
    BUYER("바이어", 2),
    VENDOR("공급사", 3),
    SUPER_BUYER("슈퍼바이어", 4);

    private final String desc;
    private final int code;
}
