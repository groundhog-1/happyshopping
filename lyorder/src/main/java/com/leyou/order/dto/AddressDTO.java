package com.leyou.order.dto;

import lombok.Data;

/**
 * @author: oyyb
 * @data: 2020-04-15
 * @version: 1.0.0
 * @descript:
 */
@Data
public class AddressDTO {

    private Long id;
    private String name;// 收件人姓名
    private String phone;// 电话
    private String state;// 省份
    private String city;// 城市
    private String district;// 区
    private String address;// 街道地址
    private String zipCode;// 邮编
    private Boolean isDefault;//

}
