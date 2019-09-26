package com.leyou;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * @author GHOSTLaycoo
 * @date 2019/9/23 0023 - 上午 10:52
 */

@SpringBootApplication
@EnableDiscoveryClient
public class LyUploadApplication {
    public static void main(String[] args) {
        SpringApplication.run(LyUploadApplication.class);
    }
}
