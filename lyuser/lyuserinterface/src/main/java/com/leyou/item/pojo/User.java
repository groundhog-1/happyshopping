package com.leyou.item.pojo;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.Pattern;
import java.util.Date;

/**
 * @author: oyyb
 * @data: 2020-04-09
 * @version: 1.0.0
 * @descript:
 */
@Table(name = "tb_user")
@Data
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Length(min = 4,max = 30,message = "用户名必须在4-30之间")
    private String username;// 用户名

    @JsonIgnore
    @Length(min = 4,max = 30,message = "密码必须在4-30之间")
    private String password;// 密码

    @Pattern(regexp = "^1[356789]\\d{9}$")
    private String phone;// 电话

    private Date created;// 创建时间

    @JsonIgnore
    private String salt;// 密码的盐值

    //TODO  add getter setters
}