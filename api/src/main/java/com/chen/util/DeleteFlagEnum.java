package com.chen.util;

/**
 * @author CHEN
 */

public enum DeleteFlagEnum {
    //Yes已经删除，No未删除
    YES("1"),
    NO("0");

    private final String value;


    DeleteFlagEnum(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
