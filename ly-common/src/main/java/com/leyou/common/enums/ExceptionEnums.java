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

    BRAND_NOT_FOUND(400,"品牌不存在"),
    CATEGORY_NOT_FOND(404,"商品分类没查到"),
    BRAND_SAVE_ERROR(500,"服务器新增品牌失败"),
    UPLOAD_FILE_ERROR(500,"文件上传失败"),
    FILE_TYPE_NOT_ERROR(400,"文件类型不匹配"),
    SPEC_GROUP_NOT_FOND(404,"商品规格组没查到"),
    SPEC_PARAM_NOT_FOND(404,"商品规格参数没查到"),
    GOODS_NOT_FOND(404,"商品不存在"),
    GOODS_SAVE_TYPE(500,"新增商品失败"),
    GOODS_DETAIL_NOT_FOND(404,"商品详情没有找到"),
    GOODS_SKU_NOT_FOND(404,"商品SKU没有找到"),
    GOODS_STOCK_NOT_FOND(404,"商品库存没找到"),
    GOODS_UPDATE_ERROR(500,"商品修改失败"),
    GOODS_ID_CANNOT_BE_NULL(400,"商品id不能为空"),
    INVALID_USER_DATA_TYPE(400,"用户数据不正确"),
    INVALID_VERIFY_CODE(400,"无效验证码"),
    INVALID_VERIFY_PASSWORD(400,"无效的用户名和密码"),
    LIN_AUTHORIZED(403,"未授权"),
    CART_NOT_FOUND(404,"用户不存在"),
    CREATE_ORDER_ERROR(500,"创建订单失败"),
    STOCK_NOT_ENOUGH(500,"库存不足异常"),
    ORDER_NOT_FOUND(500,"订单不存在"),
    WX_PAY_ORDER_FAIL(500,"微信订单失败"),
    ORDER_STATUS_ERROR(400,"订单有误"),
    INVALID_SIGN_ERROR(400,"无效签名"),
    INVALID_ORDER_PARAM(400,"订单参数异常"),
    UPDATE_ORDER_STATUS_ERROR(500,"更新订单状态失败")

    ;
    private int code;
    private String msg;

}
