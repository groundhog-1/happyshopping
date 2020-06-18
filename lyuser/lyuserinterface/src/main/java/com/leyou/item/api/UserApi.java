package com.leyou.item.api;

import com.leyou.item.pojo.User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @author: oyyb
 * @data: 2020-04-11
 * @version: 1.0.0
 * @descript:
 */
public interface UserApi {
    @GetMapping("query")
    public User queryUser(@RequestParam("username")String username, @RequestParam("password")String password);


}
