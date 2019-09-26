package com.leyou.item.mapper;

import com.leyou.item.pojo.Category;
import tk.mybatis.mapper.additional.idlist.IdListMapper;
import tk.mybatis.mapper.common.Mapper;

/**
 * @author GHOSTLaycoo
 * @date 2019/9/18 0018 - 下午 23:29
 */
public interface CategoryMapper extends Mapper<Category>, IdListMapper<Category,Long>{
}
