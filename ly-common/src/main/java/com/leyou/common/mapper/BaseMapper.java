package com.leyou.common.mapper;

import tk.mybatis.mapper.additional.idlist.IdListMapper;
import tk.mybatis.mapper.additional.insert.InsertListMapper;
import tk.mybatis.mapper.annotation.RegisterMapper;
import tk.mybatis.mapper.common.Mapper;


/**
 * @author GHOSTLaycoo
 * @date 2019/9/26 0026 - 下午 13:23
 */

@RegisterMapper
public interface BaseMapper<T> extends Mapper<T>,IdListMapper<T,Long>,InsertListMapper<T>{
}
