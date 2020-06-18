package com.leyou.order.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author: oyyb
 * @data: 2020-04-15
 * @version: 1.0.0
 * @descript:
 */
@Data
@ConfigurationProperties(prefix = "ly.worker")
public class IdWorkerProperties {

    private long worderId; //当前机器id
    private long dataCenterId;//序列号
}
