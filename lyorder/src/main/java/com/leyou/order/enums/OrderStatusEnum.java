package com.leyou.order.enums;

/**
 * @author: oyyb
 * @data: 2020-04-15
 * @version: 1.0.0
 * @descript:
 */
public enum OrderStatusEnum {
    UN_PAY(1,"未付款"),
    PAYED(2,"已付款，为发货"),
    DELIVERED(3,"已发货，未确认"),
    SUCCESS(4,"已确认，未评价"),
    CLOSED(5,"已关闭，交易失败"),
    RATED(6,"已评价")
    ;

    private int code;
    private String desc;

    OrderStatusEnum(int code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public int value(){
        return code;
    }
}
