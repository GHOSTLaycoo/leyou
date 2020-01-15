package com.leyou.order.enums;

/**
 * @author GHOSTLaycoo
 * @date 2020/1/15 0015 - 上午 10:44
 */
public enum OrderStatusEnum {
    UN_PAY(1,"未付款"),
    PAYED(2,"已付款,未发货"),
    DELIVERED(3,"已发货,未确认"),
    SUCCESS(4,"已确定,未评价"),
    CLOSE(5,"已关闭,交易失败"),
    RATED(6,"已评价")

    ;
    private int code;
    private String desc;

    OrderStatusEnum(int code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public int value(){
        return this.code;
    }
}
