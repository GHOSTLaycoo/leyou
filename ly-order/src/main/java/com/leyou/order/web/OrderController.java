package com.leyou.order.web;

import com.leyou.order.dto.OrderDTO;
import com.leyou.order.pojo.Order;
import com.leyou.order.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * @author GHOSTLaycoo
 * @date 2020/1/14 0014 - 下午 17:13
 */
@RestController
@RequestMapping("order")
public class OrderController {

    @Autowired
    private OrderService orderService;

    /**
     * 创建订单
     * @param orderDTO
     * @return
     */
    @PostMapping
    public ResponseEntity<Long> createOrder(@RequestBody OrderDTO orderDTO){
        return ResponseEntity.ok(orderService.createOrder(orderDTO));
    }

    /**
     * 根据id查询订单
     * @param id
     * @return
     */
    @GetMapping("{id}")
    public ResponseEntity<Order> queryOrderById(@PathVariable("id")Long id){
        return ResponseEntity.ok(orderService.queryOrderById(id));
    }

    /**
     * 创建支付链接
     * @param orderId
     * @return
     */
    @GetMapping("/url/{id}")
    public ResponseEntity<String> createPayUrl(@PathVariable("id")Long orderId){
        return ResponseEntity.ok(orderService.createPayUrl(orderId));
    }

    @GetMapping("/state/{id}")
    public ResponseEntity<Integer> queryOrderState(@PathVariable("id")Long id){
        return ResponseEntity.ok(orderService.queryOrderState(id).getValue());
    }
}