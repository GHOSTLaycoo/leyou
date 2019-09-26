package com.leyou.item.service;

import com.leyou.common.enums.ExceptionEnums;
import com.leyou.common.exception.LyException;
import com.leyou.item.mapper.CategoryMapper;
import com.leyou.item.pojo.Category;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.List;

/**
 * @author GHOSTLaycoo
 * @date 2019/9/18 0018 - 下午 23:31
 */

@Service
public class CategoryService {

    @Autowired
    private CategoryMapper categoryMapper;

    public List<Category> queryCategoryListByPid(Long pid) {
        Category t = new Category();
        t.setParentId(pid);
        List<Category> list = categoryMapper.select(t);
        if(CollectionUtils.isEmpty(list)){
            throw new LyException(ExceptionEnums.CATEGORY_NOT_FOND);
        }
        return  list;
    }

    public List<Category> queryByIds(List<Long> ids){
        List<Category> list = categoryMapper.selectByIdList(ids);
        if(CollectionUtils.isEmpty(list)){
            throw new LyException(ExceptionEnums.CATEGORY_NOT_FOND);
        }
        return list;
    }
}
