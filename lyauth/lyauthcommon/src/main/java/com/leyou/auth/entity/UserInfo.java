package com.leyou.auth.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author: oyyb
 * @data: 2020-04-10
 * @version: 1.0.0
 * @descript:
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserInfo {
    private Long id;

    private String username;
}
