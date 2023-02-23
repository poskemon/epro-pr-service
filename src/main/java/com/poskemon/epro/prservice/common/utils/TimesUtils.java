package com.poskemon.epro.prservice.common.utils;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class TimesUtils {

    private static final String DATETIME_FORMAT = "yyyy-MM-dd";

    /**
     * LocalDateTime 객체를 문자열로 변환합니다.
     *
     * @param localDateTime 변환할 LocalDateTime 객체
     * @return LocalDateTime 객체를 DATETIME_FORMAT 형식의 문자열로 변환한 값
     */
    public static String toString(LocalDateTime localDateTime) {
        return toString(localDateTime, DATETIME_FORMAT);
    }

    /**
     * LocalDateTime 객체를 주어진 형식의 문자열로 변환합니다.
     *
     * @param localDateTime 변환할 LocalDateTime 객체
     * @param format        문자열로 변환할 때 사용할 형식
     * @return LocalDateTime 객체를 주어진 형식의 문자열로 변환한 값
     */
    public static String toString(LocalDateTime localDateTime, String format) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(format);
        return localDateTime.format(formatter);
    }

}
