package com.leyou.test;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.HashMap;

/**
 * @author GHOSTLaycoo
 * @date 2020/1/6 0006 - 下午 16:19
 */

@RunWith(SpringRunner.class)
@SpringBootTest
public class SmsTest {

    @Autowired
    private AmqpTemplate amqpTemplate;

    @Test
    public void testSend() throws InterruptedException {
        HashMap<String, String> msg = new HashMap<>();
        msg.put("phone","13554401527");
        msg.put("code","54321");
        amqpTemplate.convertAndSend("ly.sms.exchange","sms.verify.code",msg);

    }
}
