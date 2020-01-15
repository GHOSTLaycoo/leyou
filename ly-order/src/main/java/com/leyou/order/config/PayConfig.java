package com.leyou.order.config;

import com.github.wxpay.sdk.WXPayConfig;
import lombok.Data;

import java.io.InputStream;

/**
 * @author GHOSTLaycoo
 * @date 2020/1/15 0015 - 下午 15:08
 */
@Data
public class PayConfig implements WXPayConfig{
    private String appID;
    private String mchID;
    private String key;
    private int httpConnectTimeoutMs;
    private int httpReadTimeoutMs;
    private String notifyUrl;

    @Override
    public InputStream getCertStream(){
        return null;
    }
}
