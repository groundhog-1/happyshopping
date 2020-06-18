package com.leyou.order.config;

import com.leyou.auth.utils.RsaUtils;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.ConfigurationProperties;

import javax.annotation.PostConstruct;
import java.security.PublicKey;

/**
 * @author: oyyb
 * @data: 2020-04-11
 * @version: 1.0.0
 * @descript:
 */
@Slf4j
@ConfigurationProperties(prefix = "ly.jwt")
@Data
@NoArgsConstructor
public class JwtProperties {

    private String pubKeyPath;// 公钥


    private PublicKey publicKey; // 公钥



    private String cookieName;


    @PostConstruct
    public void init(){
        try {
            // 获取公钥和私钥
            this.publicKey = RsaUtils.getPublicKey(pubKeyPath);

        } catch (Exception e) {
            log.error("初始化公钥和私钥失败！", e);
            throw new RuntimeException();
        }
    }
}
