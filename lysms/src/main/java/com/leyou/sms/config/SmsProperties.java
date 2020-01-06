package com.leyou.sms.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author GHOSTLaycoo
 * @date 2019/12/24 0024 - 下午 14:18
 */

@Data
@ConfigurationProperties(prefix = "ly.sms")
public class SmsProperties {
    String accessKeyId;

    String accessKeySecret;

    String signName;

    String verifyCodeTemplate;
}
