package com.poskemon.epro.prservice.common.utils;

public class CurrencyUtils {

    /**
     * 숫자를 천 단위로 구분하고 "원" 단위를 붙여서 반환합니다.
     *
     * @param number "원" 단위를 붙일 숫자
     * @return "원" 단위가 붙은 문자열
     */
    public static String toKRWCurrency(Long number) {
        return String.format("%,d원", number);
    }

}