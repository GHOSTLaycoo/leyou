package com.leyou.order.enums;

/**
 * @author GHOSTLaycoo
 * @date 2020/1/15 0015 - 下午 19:41
 */
public enum PayState {
    NOT_PAY(0),SUCCESS(1),FAIL(2);

    int value;

    PayState(int value){
        this.value = value;
    }

    public int getValue(){
        return value;
    }
}
