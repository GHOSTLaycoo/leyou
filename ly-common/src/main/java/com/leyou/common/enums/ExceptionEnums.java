package com.leyou.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * @author GHOSTLaycoo
 * @date 2019/9/18 0018 - 下午 23:12
 */

@Getter
@NoArgsConstructor
@AllArgsConstructor
public enum ExceptionEnums {

    PRICE_CANNOT_BE_NULL(400,"价格不能为空!"),
    CATEGORY_NOT_FOND(404,"商品分类没查到"),
    ;
    private int code;
    private String msg;

}
