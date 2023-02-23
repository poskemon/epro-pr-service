package com.poskemon.epro.prservice.exception;

public class PrLineException extends IllegalArgumentException {
    private static final String ERROR = "유효하지 않는 PrLine 정보 입니다.";

    public PrLineException() {
        super(ERROR);
    }
}
