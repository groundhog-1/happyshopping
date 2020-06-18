package com.leyou.sms.utils;


import com.aliyuncs.exceptions.ClientException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.HashMap;
import java.util.Map;

/**
 * @author: oyyb
 * @data: 2020-04-08
 * @version: 1.0.0
 * @descript:
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class SmsUtilsTest {

    @Autowired
    private AmqpTemplate amqpTemplate;

    @Autowired
    private SmsUtils smsUtils;


    @Test
    public void testSend() throws InterruptedException {
        Map<String,String> map = new HashMap<>();
        map.put("phone","13017264220");
        map.put("code","54321");
        amqpTemplate.convertAndSend("ly.sms.exchange","sms.verify.code",map);
        Thread.sleep(10000L);
//        try {
//            smsUtils.sendSms("17680727094","乐友商城","SMS_187540764","{'code':'66666'}");
//        } catch (ClientException e) {
//            e.printStackTrace();
//        }

    }

}