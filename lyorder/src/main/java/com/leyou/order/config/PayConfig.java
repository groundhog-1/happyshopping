package com.leyou.order.config;

import com.github.wxpay.sdk.WXPayConfig;
import lombok.Data;
import java.io.InputStream;


@Data
public class PayConfig implements WXPayConfig {

    private String appId; // 公众账号ID

    private String mchId; // 商户号

    private String key; // 生成签名的密钥

    private int connectTimeoutMs; // 连接超时时间

    private int readTimeoutMs;// 读取超时时间

    private String notifyUrl;// 下单通知调回地址

    @Override
    public String getAppID() {
        return appId;
    }

    @Override
    public String getMchID() {
        return mchId;
    }

    @Override
    public String getKey() {
        return key;
    }

    @Override
    public InputStream getCertStream() {
        return null;
    }

    @Override
    public int getHttpConnectTimeoutMs() {
        return connectTimeoutMs;
    }

    @Override
    public int getHttpReadTimeoutMs() {
        return readTimeoutMs;
    }
}
