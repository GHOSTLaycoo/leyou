package com.leyou.item.service;


import com.leyou.common.dto.CartDTO;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Arrays;
import java.util.List;

/**
 * @author GHOSTLaycoo
 * @date 2020/1/15 0015 - 上午 11:39
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class GoodsServiceTest {

    @Autowired
    private GoodsService goodsService;

    @org.junit.Test
    public void decreseStock() {
        List<CartDTO> cartDTOS = Arrays.asList(new CartDTO(2600242L,2),new CartDTO(2600248L,2));
        goodsService.decreseStock(cartDTOS);
    }
}