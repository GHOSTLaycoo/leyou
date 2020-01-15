package com.leyou.order.web;

import com.leyou.order.service.OrderService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

/**
 * @author GHOSTLaycoo
 * @date 2020/1/15 0015 - 下午 16:39
 */
@RestController
@RequestMapping("notify")
@Slf4j
public class NotifyController {

    @Autowired
    private OrderService orderService;

    /**
     * 微信支付成功反馈
     * @param result
     * @return
     */
    @PostMapping(value = "pay",produces = "application/xml")
    public Map<String,String> hello(@RequestBody Map<String,String> result){
        orderService.handleNotify(result);

        log.info("[支付回调] 接收微信支付回调,结果:{}",result);

        Map<String,String> msg = new HashMap<>();
        msg.put("return_code","SUCCESS");
        msg.put("return_msg","OK");
        return msg;
    }

    @GetMapping(value = "{id}")
    public String hell(@PathVariable("id")Long id){
        return "id:"+ id;
    }
}
