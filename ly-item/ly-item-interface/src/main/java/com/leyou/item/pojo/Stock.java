package com.leyou.item.pojo;

import lombok.Data;

import javax.persistence.Id;
import javax.persistence.Table;

/**
 * @author GHOSTLaycoo
 * @date 2019/9/26 0026 - 下午 12:55
 */

@Data
@Table(name = "tb_stock")
public class Stock {
    @Id
    private Long skuId;
    private Integer seckillStock;// 秒杀可用库存
    private Integer seckillTotal;// 已秒杀数量
    private Integer stock;// 正常库存
}
