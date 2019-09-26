package com.leyou.item.service;

import com.leyou.common.enums.ExceptionEnums;
import com.leyou.common.exception.LyException;
import com.leyou.item.mapper.SpecGroupMapper;
import com.leyou.item.mapper.SpecParamMapper;
import com.leyou.item.pojo.SpecGroup;
import com.leyou.item.pojo.SpecParam;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.List;

/**
 * @author GHOSTLaycoo
 * @date 2019/9/25 0025 - 下午 15:03
 */

@Service
public class SpecificationService {

    @Autowired
    private SpecGroupMapper groupMapper;

    @Autowired
    private SpecParamMapper specParamMapper;

    public List<SpecGroup> queryGroupByCid(Long cid) {
        SpecGroup specGroup = new SpecGroup();
        specGroup.setCid(cid);
        List<SpecGroup> list = groupMapper.select(specGroup);

        if(CollectionUtils.isEmpty(list)){
            throw new LyException(ExceptionEnums.SPEC_GROUP_NOT_FOND);
        }
        return list;
    }

    public List<SpecParam> queryParamList(Long gid, Long cid, Boolean searching) {
        SpecParam specParam = new SpecParam();
        specParam.setGroupId(gid);
        specParam.setCid(cid);
        specParam.setSearching(searching);
        List<SpecParam> list = specParamMapper.select(specParam);
        if(CollectionUtils.isEmpty(list)){
            throw new LyException(ExceptionEnums.SPEC_PARAM_NOT_FOND);
        }
        return list;
    }
}
