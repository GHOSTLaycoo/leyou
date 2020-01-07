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

    ;
    private int code;
    private String msg;

}
