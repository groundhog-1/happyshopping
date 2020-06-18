package com.leyou.order.web;

import com.leyou.order.service.OrderService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

/**
 * @author: oyyb
 * @data: 2020-04-16
 * @version: 1.0.0
 * @descript:
 */
@RestController
@RequestMapping("notify")
@Slf4j
public class NotifyController {

    @Autowired
    private OrderService orderService;

    @PostMapping(value = "pay", produces = "application.xml")
    @ResponseBody
    public Map<String,String> hello(@RequestBody Map<String,String> result){
        //处理回调
        orderService.handleNotify(result);

        log.info("[支付回调] 微信支付回调，结果：{}",result);
        Map<String,String> msg = new HashMap<>();
        msg.put("return_code","SUCCESS");
        msg.put("return_nsg","ok");

        return msg;
    }

}
