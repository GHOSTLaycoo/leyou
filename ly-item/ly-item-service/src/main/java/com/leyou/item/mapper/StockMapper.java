package com.leyou.item.mapper;

import com.leyou.common.mapper.BaseMapper;
import com.leyou.item.pojo.Stock;
import tk.mybatis.mapper.additional.insert.InsertListMapper;

/**
 * @author GHOSTLaycoo
 * @date 2019/9/26 0026 - 下午 13:01
 */
public interface StockMapper extends InsertListMapper<Stock>,BaseMapper<Stock> {
}
