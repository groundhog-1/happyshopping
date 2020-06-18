package com.leyou.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * @author: oyyb
 * @data: 2020-03-17
 * @version: 1.0.0
 * @descript:
 */
@Getter
@NoArgsConstructor
@AllArgsConstructor
public enum ExceptionEnum {
    CATEGORY_NOT_FOUND(404,"商品没有查到"),
    BRAND_NOT_FOUND(404,"品牌没找到" ),
    BRAND_SAVE_ERROR(500,"新增品牌失败"),
    INVALID_FILE_TYPE(500,"无效的文件类型"),
    SPECGROUP_NOT_FOUND(404,"商品规格组没有查到"),
    SPEC_PARAM_NOT_FOUND(404,"商品规格参数不存在"),
    GOODS_SKU_NOT_FOUND(404,"商品SKU不存在"),
    UPLOAD_FILE_ERROR(500,"文件上传失败"),
    GOODS_SAVE_TYPE(500,"新增商品失败"),

    GOODS_NOT_FOUND(404,"商品未被发现"),
    GOODS_UPDATE_ERROR(500,"更新商品失败" ),
    GOODS_ID_CANNOT_NULL(404,"商品Id不能为空" ),
    INVALID_USER_DATA_TYPE(400,"用户数据类型无效" ),
    CART_NOT_FOUND(404,"购物车未被发现"),
    CREATE_ORDER_ERROR(500,"创建订单失败"),
    STOCK_NOT_ENOUGH(500,"库存不足"),
    ORDER_NOT_FOUND(500,"订单未被发现" ),
    ORDER_DETIAL_NOT_FOUND(500,"订单详情未被发现" ),
    ORDER_STATUS_NOT_FOUND(500,"订单状态未被发现"),
    WX_PAY_ORDER_FAIL(500,"微信下单失败" ),
    ORDER_STATUS_ERROR(500,"订单状态有误" ),
    INVALID_SIGN_ERROR(400,"签名有误" ),
    INVALID_ORDER_PARAM(400,"订单参数异常" ),
    UPDATE_ORDER_STATUS_ERROR(400,"跟新订单状态失败" );
    private int code;
    private String msg;


}
