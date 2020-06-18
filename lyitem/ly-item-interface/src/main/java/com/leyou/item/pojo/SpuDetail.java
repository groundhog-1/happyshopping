package com.leyou.item.pojo;

import lombok.Data;

import javax.persistence.Id;
import javax.persistence.Table;

/**
 * @author: oyyb
 * @data: 2020-03-29
 * @version: 1.0.0
 * @descript:
 */
@Table(name="tb_spu_detail")
@Data
public class SpuDetail {
    @Id
    private Long spuId;// 对应的SPU的id
    private String description;// 商品描述
    private String specialSpec;// 商品特殊规格的名称及可选值模板
    private String packingList;// 包装清单
    private String afterService;// 售后服务
    private String genericSpec;
    // 省略getter和setter
}
