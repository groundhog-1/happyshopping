package com.leyou.item.service;

import com.leyou.common.enums.ExceptionEnum;
import com.leyou.common.exception.LyException;
import com.leyou.item.mapper.CategoryMapper;
import com.leyou.item.mapper.SpecGroupMapper;
import com.leyou.item.mapper.SpecParamMapper;
import com.leyou.item.pojo.SpecGroup;
import com.leyou.item.pojo.SpecParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author: oyyb
 * @data: 2020-03-29
 * @version: 1.0.0
 * @descript:
 */
@Service
public class SpecificationService {
    @Autowired
    private SpecGroupMapper specGroupMapper;

    @Autowired
    private SpecParamMapper paramMapper;

    public List<SpecGroup> queryGroupByCid(Long cid) {
        SpecGroup specGroup=new SpecGroup();
        specGroup.setCid(cid);
        List<SpecGroup> list = specGroupMapper.select(specGroup);
        if (CollectionUtils.isEmpty(list)){
            throw new LyException(ExceptionEnum.SPECGROUP_NOT_FOUND);
        }
        return list;
    }


    public List<SpecParam> queryParamList(Long gid, Long cid, Boolean searching) {
        SpecParam specParam = new SpecParam();
        specParam.setGroupId(gid);
        specParam.setCid(cid);
        specParam.setSearching(searching);
        List<SpecParam> list = paramMapper.select(specParam);
        if (CollectionUtils.isEmpty(list)){
            throw new LyException(ExceptionEnum.SPEC_PARAM_NOT_FOUND);
        }
        return list;
    }

    public List<SpecGroup> queryListByCid(Long cid) {
        //查询规格组
        List<SpecGroup> groups = queryGroupByCid(cid);
        //查询组内参数
        List<SpecParam> specParams = queryParamList(null, cid, null);
        //先把规格参数变成map,map的key是规格组id，map的值是组下的所有参数
        Map<Long,List<SpecParam>> params =new HashMap<>();
        for (SpecParam specParam : specParams) {
            if (!params.containsKey(specParam.getGroupId())){
                //这个组id在map中不存在
                params.put(specParam.getGroupId(),new ArrayList<>());
            }
            params.get(specParam.getGroupId()).add(specParam);
        }

        //填充SpecParam到SpecGroup中
        for (SpecGroup group : groups) {
            group.setParams(params.get(group.getId()));
        }

        return groups;
    }
}
