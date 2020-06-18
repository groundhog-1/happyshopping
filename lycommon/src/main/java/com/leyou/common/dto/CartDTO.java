package com.leyou.common.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author: oyyb
 * @data: 2020-04-15
 * @version: 1.0.0
 * @descript:
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CartDTO {
    private Long skuId;//商品skuId
    private Integer num;//购买数量
}
